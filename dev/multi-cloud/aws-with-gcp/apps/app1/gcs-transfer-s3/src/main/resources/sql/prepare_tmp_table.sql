BEGIN
-- CREATE VARIABLES
DECLARE step1_temp_table_name_with_datetime_stamp STRING;
DECLARE step2_temp_table_name_with_datetime_stamp STRING;
DECLARE step3_temp_table_name_with_datetime_stamp STRING;
DECLARE padding_sec_format STRING;
DECLARE parse_time_format STRING;
DECLARE timestamp_format STRING;
SET step1_temp_table_name_with_datetime_stamp = 'step1_temp_table_' || FORMAT_DATE('%Y%m%d%H%M%S', CURRENT_DATETIME('Asia/Tokyo'));
SET step2_temp_table_name_with_datetime_stamp = 'step2_temp_table_' || FORMAT_DATE('%Y%m%d%H%M%S', CURRENT_DATETIME('Asia/Tokyo'));
SET step3_temp_table_name_with_datetime_stamp = 'step3_temp_table_' || FORMAT_DATE('%Y%m%d%H%M%S', CURRENT_DATETIME('Asia/Tokyo'));
SET padding_sec_format = "%02d";
SET parse_time_format = "%Y%m%d%H%M";
SET timestamp_format = "%Y-%m-%dT%H:%M:%SZ";

-- TRUNCATE TMP_NONTCU_UCD_HISTORY
TRUNCATE TABLE `ddl_test.TMP_NONTCU_UCD_HISTORY`;

-- CREATE STEP0 TEMP_TABLE
-- 概要: 契約者テーブル情報とVEHICLE_IDマスタを結合してIMEI,MODEL_YEAR等を持ち回る。
EXECUTE IMMEDIATE FORMAT("""
CREATE TEMP TABLE `_SESSION.%s` AS
SELECT
       a.VEHICLE_ID AS VEHICLE_ID,
       a.CONTRACTOR AS CONTRACTOR,
       b.IMEI AS IMEI,
       b.MODEL_YEAR AS MODEL_YEAR
   FROM
        (
          SELECT
                 *
          FROM
               `ddl_test.MSPFD_CONTRACT_VEHICLES_LIST`
          WHERE
                CONTRACTOR = '${contractor_name}'
        ) a
INNER JOIN
           `ddl_test.NONTCU_VEHICLE_ID_MASTER` b
ON a.VEHICLE_ID = b.VEHICLE_ID
;
""", step3_temp_table_name_with_datetime_stamp);

-- CREATE STEP1 TEMP_TABLE
-- 概要: timestampで重複するレコードを削除する
EXECUTE IMMEDIATE FORMAT("""
CREATE TEMP TABLE `_SESSION.%s` AS
SELECT
      b.imei AS imei,
      b.timestamp AS timestamp,
      b.longitude AS longitude,
      b.latitude AS latitude,
      b.car_speed AS car_speed,
      b.pcap AS pcap,
      b.bat_pcap AS bat_pcap,
      b.batt_v_total AS batt_v_total,
      b.soc_disp AS soc_disp,
      b.odometer AS odometer,
      b.vehicle_status AS vehicle_status,
      b.obc_in_i AS obc_in_i,
      b.obc_in_v AS obc_in_v,
      b.dist2empty AS dist2empty,
      b.batch_exe_timestamp AS batch_exe_timestamp
FROM
(
     SELECT
            a.*,
            -- timestampが重複するレコードを排除する
            ROW_NUMBER() OVER (
               PARTITION BY a.imei, a.timestamp
               ORDER BY
                        a.timestamp DESC
            ) AS rn
     FROM
          (
            SELECT
                   imei,
                   timestamp,
                   longitude,
                   latitude,
                   car_speed,
                   pcap,
                   bat_pcap,
                   batt_v_total,
                   soc_disp,
                   odometer,
                   vehicle_status,
                   obc_in_i,
                   obc_in_v,
                   dist2empty,
                   batch_exe_timestamp
            FROM
                 `ddl_test.view_car_state_position_summary`
            WHERE
                  DATE(batch_exe_timestamp) = DATE(${year},${month},${day})
             AND
                 imei IN (
                           SELECT
                                  IMEI
                             FROM
                                  `_SESSION.%s`
                         )
          ) AS a
  WHERE
        TRUE QUALIFY rn = 1
) AS b;
""", step1_temp_table_name_with_datetime_stamp, step3_temp_table_name_with_datetime_stamp);

-- CREATE STEP2 TEMP_TABLE
-- 概要: 各レコードを10秒1レコードに絞り込む
EXECUTE IMMEDIATE FORMAT("""
CREATE TEMP TABLE `_SESSION.%s` AS
SELECT
       c.imei,
       c.timestamp,
       c.longitude,
       c.latitude,
       c.car_speed,
       c.pcap,
       c.bat_pcap,
       c.batt_v_total,
       c.soc_disp,
       c.odometer,
       c.vehicle_status,
       c.obc_in_i,
       c.obc_in_v,
       c.dist2empty,
       c.batch_exe_timestamp
FROM
     (
       SELECT
             -- 10秒1レコード(10秒間で最も古いレコード)を抽出する
             b.*,
             RANK() OVER (PARTITION BY imei, sec_group ORDER BY timestamp ASC) AS rn
       FROM
            (
              SELECT
                    a.*,
                    parsetm || RPAD(SUBSTR(padnum, 0, 1), 2, '0') AS sec_group
               FROM
                    (
                      SELECT
                             imei,
                             timestamp,
                             longitude,
                             latitude,
                             car_speed,
                             pcap,
                             bat_pcap,
                             batt_v_total,
                             soc_disp,
                             odometer,
                             vehicle_status,
                             obc_in_i,
                             obc_in_v,
                             dist2empty,
                             batch_exe_timestamp,
                             FORMAT("%s", EXTRACT(SECOND FROM timestamp)) AS padnum,
                             FORMAT_TIMESTAMP("%s", timestamp, "UTC") AS parsetm
                     FROM
                          `_SESSION.%s`
                    ) AS a
               ORDER BY sec_group, timestamp
            ) AS b
     QUALIFY rn = 1
     ORDER BY timestamp
) AS c;
""", step2_temp_table_name_with_datetime_stamp, padding_sec_format, parse_time_format, step1_temp_table_name_with_datetime_stamp);

-- CREATE STEP3 INSERT DATA
-- 概要: SOHを計算したレコードをTMP_NONTCU_UCD_HISTORYに挿入する
EXECUTE IMMEDIATE FORMAT("""
INSERT INTO `ddl_test.TMP_NONTCU_UCD_HISTORY`
SELECT
       c.VEHICLE_ID AS vehicleId,
       FORMAT_TIMESTAMP("%s", c.timestamp) AS dataTimestamp,
       TRUNC(CAST(c.soc_disp AS FLOAT64), 1) AS stateOfCharge,
       CASE
            WHEN c.vehicle_status = "normal_charging" OR c.vehicle_status = "quick_charging" THEN 2
            WHEN c.vehicle_status = "na" THEN 4
            ELSE 0
       END AS plugConnectionStatus,
       CASE
            WHEN c.latitude = "" THEN NULL
            ELSE TRUNC(CAST(c.latitude AS FLOAT64), 6)
       END AS latitude,
       CASE
            WHEN c.longitude = "" THEN NULL
            ELSE TRUNC(CAST(c.longitude AS FLOAT64), 6)
       END AS longitude,
       TRUNC(CAST(c.odometer AS FLOAT64), 1) AS odometer,
       CASE
            WHEN c.MODEL_YEAR = 23.0 THEN TRUNC((c.pcap * (330/1000) * (c.soc_disp/100)), 1)
            ELSE TRUNC((c.bat_pcap * (c.soc_disp/100)), 1)
       END AS batteryCapacity,
       c.batch_exe_timestamp AS batchExeTimestamp
FROM
     (
       SELECT
              b.VEHICLE_ID,
              b.MODEL_YEAR,
              a.timestamp,
              a.longitude,
              a.latitude,
              a.car_speed,
              a.pcap,
              a.bat_pcap,
              a.batt_v_total,
              a.soc_disp,
              a.odometer,
              a.vehicle_status,
              a.obc_in_i,
              a.obc_in_v,
              a.dist2empty,
              a.batch_exe_timestamp
       FROM
            `_SESSION.%s` a
       INNER JOIN
            `_SESSION.%s` b
       ON a.imei = b.IMEI
     ) AS c
ORDER BY vehicleId, dataTimestamp ASC;
""", timestamp_format, step2_temp_table_name_with_datetime_stamp, step3_temp_table_name_with_datetime_stamp);

-- DROP TEMP TABLE
EXECUTE IMMEDIATE FORMAT("""
DROP TABLE `_SESSION.%s`;
""", step1_temp_table_name_with_datetime_stamp);

-- DROP TEMP TABLE
EXECUTE IMMEDIATE FORMAT("""
DROP TABLE `_SESSION.%s`;
""", step2_temp_table_name_with_datetime_stamp);

-- DROP TEMP TABLE
EXECUTE IMMEDIATE FORMAT("""
DROP TABLE `_SESSION.%s`;
""", step3_temp_table_name_with_datetime_stamp);

END