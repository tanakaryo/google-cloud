package com.example.demo.application.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

import com.example.demo.adapter.out.persistence.HistoryDataRepository;
import com.example.demo.application.domain.service.impl.HistoryDataCreateServiceImpl;

public class HistoryDataCreateServiceImplTest {

    @Test
    @DisplayName("initialize 正常系テスト1: initializeメソッド呼び出し検証")
    public void test01() throws Exception {

        HistoryDataCreateServiceImpl target = new HistoryDataCreateServiceImpl();

        HistoryDataRepository historyDataRepositoryMock = Mockito.mock(HistoryDataRepository.class);
        doNothing().when(historyDataRepositoryMock).initialize(any());

        Field f = target.getClass().getDeclaredField("historyDataReadPort");
        f.setAccessible(true);
        f.set(target, historyDataRepositoryMock);

        try {
            target.initialize(new Properties());
            Assertions.assertTrue(true);
        } catch (Exception e) {
            Assertions.fail();
        }

    }

    @Test
    @DisplayName("execute 正常系テスト2: executeメソッド呼び出し検証(records=nullケース)")
    public void test02() throws Exception {

        HistoryDataCreateServiceImpl target = new HistoryDataCreateServiceImpl();

        HistoryDataRepository historyDataRepositoryMock = Mockito.mock(HistoryDataRepository.class);
        when(historyDataRepositoryMock.findById(anyString())).thenReturn(new ArrayList<>());

        Field f = target.getClass().getDeclaredField("historyDataReadPort");
        f.setAccessible(true);
        f.set(target, historyDataRepositoryMock);

        target.initialize(new Properties());
        String actual = target.execute("DUMMY");

        Assertions.assertNull(actual);
    }

    @Test
    @DisplayName("execute 正常系テスト2: executeメソッド呼び出し検証(records<>nullケース)")
    public void test03() throws Exception {

        HistoryDataCreateServiceImpl target = new HistoryDataCreateServiceImpl();

        List<String> mockResult = new ArrayList<>();
        mockResult.add("{\\\"dataTimestamp\\\": \\\"2025-05-28T16:50:19Z\\\", \\\"stateOfCharge\\\": 91.2, \\\"plugConnectionStatus\\\": 2, \\\"latitude\\\": 43.071049, \\\"longitude\\\": 141.370823, \\\"odometer\\\": 10416.0, \\\"batteryCapacity\\\": 12.1},, {\\\"dataTimestamp\\\": \\\"2025-05-28T16:20:20Z\\\", \\\"stateOfCharge\\\": 91.2, \\\"plugConnectionStatus\\\": 2, \\\"latitude\\\": 43.071049, \\\"longitude\\\": 141.370823, \\\"odometer\\\": 10416.0, \\\"batteryCapacity\\\": 12.1}");
        mockResult.add("{\\\"dataTimestamp\\\": \\\"2025-05-28T16:50:19Z\\\", \\\"stateOfCharge\\\": 91.2, \\\"plugConnectionStatus\\\": 2, \\\"latitude\\\": 43.071049, \\\"longitude\\\": 141.370823, \\\"odometer\\\": 10416.0, \\\"batteryCapacity\\\": 12.1},, {\\\"dataTimestamp\\\": \\\"2025-05-28T16:20:20Z\\\", \\\"stateOfCharge\\\": 91.2, \\\"plugConnectionStatus\\\": 2, \\\"latitude\\\": 43.071049, \\\"longitude\\\": 141.370823, \\\"odometer\\\": 10416.0, \\\"batteryCapacity\\\": 12.1}");

        HistoryDataRepository historyDataRepositoryMock = Mockito.mock(HistoryDataRepository.class);
        when(historyDataRepositoryMock.findById(anyString())).thenReturn(mockResult);

        Field f = target.getClass().getDeclaredField("historyDataReadPort");
        f.setAccessible(true);
        f.set(target, historyDataRepositoryMock);

        target.initialize(new Properties());
        String actual = target.execute("8192c452-a0e8-4263-9241-39d3e118646d");

        Assertions.assertEquals("{ \"resultItems\":  [{\"vehicleId\": \"8192c452-a0e8-4263-9241-39d3e118646d\", \"vehicleData\":[{\\\"dataTimestamp\\\": \\\"2025-05-28T16:50:19Z\\\", \\\"stateOfCharge\\\": 91.2, \\\"plugConnectionStatus\\\": 2, \\\"latitude\\\": 43.071049, \\\"longitude\\\": 141.370823, \\\"odometer\\\": 10416.0, \\\"batteryCapacity\\\": 12.1},, {\\\"dataTimestamp\\\": \\\"2025-05-28T16:20:20Z\\\", \\\"stateOfCharge\\\": 91.2, \\\"plugConnectionStatus\\\": 2, \\\"latitude\\\": 43.071049, \\\"longitude\\\": 141.370823, \\\"odometer\\\": 10416.0, \\\"batteryCapacity\\\": 12.1}, {\\\"dataTimestamp\\\": \\\"2025-05-28T16:50:19Z\\\", \\\"stateOfCharge\\\": 91.2, \\\"plugConnectionStatus\\\": 2, \\\"latitude\\\": 43.071049, \\\"longitude\\\": 141.370823, \\\"odometer\\\": 10416.0, \\\"batteryCapacity\\\": 12.1},, {\\\"dataTimestamp\\\": \\\"2025-05-28T16:20:20Z\\\", \\\"stateOfCharge\\\": 91.2, \\\"plugConnectionStatus\\\": 2, \\\"latitude\\\": 43.071049, \\\"longitude\\\": 141.370823, \\\"odometer\\\": 10416.0, \\\"batteryCapacity\\\": 12.1}]}]}", actual);
    }    
}
