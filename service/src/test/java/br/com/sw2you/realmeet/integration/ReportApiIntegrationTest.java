package br.com.sw2you.realmeet.integration;

import br.com.sw2you.realmeet.api.facade.ReportApi;
import br.com.sw2you.realmeet.core.BaseIntegrationTest;
import br.com.sw2you.realmeet.domain.repository.AllocationRepository;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import br.com.sw2you.realmeet.model.TestReportDispatcherService;
import br.com.sw2you.realmeet.report.enumeration.ReportFormat;
import br.com.sw2you.realmeet.service.ReportDispatcherService;
import br.com.sw2you.realmeet.utils.ConstantsTest;
import br.com.sw2you.realmeet.utils.TestDataCreator;
import java.time.LocalDate;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(ReportApiIntegrationTest.Configuration.class)
public class ReportApiIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private ReportApi api;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AllocationRepository allocationRepository;

    @Override
    protected void setupEach() throws Exception {
        setLocalHostBasePath(api.getApiClient(), "/v1");
    }

    @Test
    void testCreateAllocationReportSuccess() {
        persistAllocations(10);

        Assertions.assertDoesNotThrow(
            () ->
                api.createAllocationReport(
                    ConstantsTest.TEST_CLIENT_API_KEY,
                    ConstantsTest.EMAIL_TO,
                    LocalDate.now().minusDays(15),
                    LocalDate.now().plusDays(15),
                    ReportFormat.PDF.name()
                )
        );
    }

    private void persistAllocations(int numberOfAllocations) {
        var room = roomRepository.saveAndFlush(TestDataCreator.newRoomBuilderDefault().build());

        IntStream
            .rangeClosed(1, numberOfAllocations)
            .forEach(
                i ->
                    allocationRepository.saveAndFlush(
                        TestDataCreator
                            .newAllocationBuilderDefault()
                            .room(room)
                            .subject(ConstantsTest.DEFAULT_ALLOCATION_SUBJECT + "-" + i)
                            .startAt(ConstantsTest.DEFAULT_ALLOCATION_START_AT.plusDays(i))
                            .endAt(ConstantsTest.DEFAULT_ALLOCATION_END_AT.plusDays(i))
                            .build()
                    )
            );
    }

    @TestConfiguration
    static class Configuration {

        @Bean
        public ReportDispatcherService reportDispatcherService() {
            return new TestReportDispatcherService();
        }
    }
}
