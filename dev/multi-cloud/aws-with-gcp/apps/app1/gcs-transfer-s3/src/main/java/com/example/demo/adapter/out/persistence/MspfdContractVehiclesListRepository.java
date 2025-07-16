package com.example.demo.adapter.out.persistence;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.example.demo.application.port.out.ListVehiclesPort;
import com.example.demo.common.constants.BatchConst;
import com.example.demo.common.utils.BigQueryUtils;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;

public class MspfdContractVehiclesListRepository implements ListVehiclesPort {

    private static final String FILE_PATH_FIND_VID_LIST = "/sql/find_vehicle_ids_by_contractor.sql";

    private static final String PLACE_HOLDER_CONTRACTOR = "${contractor_name}";

    private Properties batchParameter;

    @Override
    public void setParameter(Properties properties) {
        this.batchParameter = properties;
    }

    @Override
    public List<String> listVehiclesByContractor() throws Exception {
        List<String> vehicleIds = new ArrayList<>();

        InputStream is = this.getClass().getResourceAsStream(FILE_PATH_FIND_VID_LIST);
        String query = new String(is.readAllBytes());
        is.close();

        query = StringUtils.replace(query, PLACE_HOLDER_CONTRACTOR, batchParameter.getProperty(BatchConst.KEY_CONTRACTOR));

        BigQuery bigQuery = BigQueryUtils.getService();
        QueryJobConfiguration configuration = BigQueryUtils.buildConfig(query);
        TableResult result = bigQuery.query(configuration);
         result.getValues().forEach((row)-> {
            vehicleIds.add(row.get("VEHICLE_ID").getStringValue());
         });

         return vehicleIds;
    }

}
