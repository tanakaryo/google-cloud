-- insert into test_table1
DECLARE x INT64 DEFAULT 0;

LOOP
     SET x = x + 1;
     IF x > 100 THEN
        LEAVE;
     END IF;

     EXECUTE IMMEDIATE
      "INSERT INTO my_dataset.test_table1 VALUES(?,?,?,?)"
      USING x, "John", 14, "JAPAN";
END LOOP;
