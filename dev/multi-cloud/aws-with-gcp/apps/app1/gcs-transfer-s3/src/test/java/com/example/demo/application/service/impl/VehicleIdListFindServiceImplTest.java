package com.example.demo.application.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.example.demo.adapter.out.persistence.MspfdContractVehiclesListRepository;
import com.example.demo.application.domain.service.impl.VehicleIdListFindServiceImpl;

public class VehicleIdListFindServiceImplTest {

    @Test
    @DisplayName("setParameter 正常系テスト1: setParameterメソッド呼び出し検証")
    public void test01() throws Exception {

        VehicleIdListFindServiceImpl target = new VehicleIdListFindServiceImpl();

        MspfdContractVehiclesListRepository mspfdContractVehiclesListRepositoryMock = Mockito
                .mock(MspfdContractVehiclesListRepository.class);
        doNothing().when(mspfdContractVehiclesListRepositoryMock).setParameter(any());

        Field f = target.getClass().getDeclaredField("listVehiclesPort");
        f.setAccessible(true);
        f.set(target, mspfdContractVehiclesListRepositoryMock);

        try {
            target.setParameter(new Properties());
            Assertions.assertTrue(true);
        } catch (Exception e) {
            Assertions.fail();
        }

    }

    @Test
    @DisplayName("execute 正常系テスト1: executeメソッド呼び出し検証")
    public void test02() throws Exception {

        VehicleIdListFindServiceImpl target = new VehicleIdListFindServiceImpl();

        List<String> resultMock = new ArrayList<>();
        resultMock.add("dummy-1");
        resultMock.add("dummy-2");

        MspfdContractVehiclesListRepository mspfdContractVehiclesListRepositoryMock = Mockito
                .mock(MspfdContractVehiclesListRepository.class);
        when(mspfdContractVehiclesListRepositoryMock.listVehiclesByContractor()).thenReturn(resultMock);

        Field f = target.getClass().getDeclaredField("listVehiclesPort");
        f.setAccessible(true);
        f.set(target, mspfdContractVehiclesListRepositoryMock);

        target.setParameter(new Properties());
        List<String> actual = target.execute();

        Assertions.assertEquals("dummy-1", actual.get(0));
        Assertions.assertEquals("dummy-2", actual.get(1));
    }
}
