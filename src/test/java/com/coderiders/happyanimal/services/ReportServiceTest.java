package com.coderiders.happyanimal.services;


import com.coderiders.happyanimal.mapper.ReportMapper;
import com.coderiders.happyanimal.model.Report;
import com.coderiders.happyanimal.model.User;
import com.coderiders.happyanimal.model.dto.ReportDto;
import com.coderiders.happyanimal.repository.ReportRepository;
import com.coderiders.happyanimal.service.ReportService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class ReportServiceTest {

    @Autowired
    private ReportService reportService;

    @MockBean
    private ReportRepository reportRepository;

    @MockBean
    private ReportMapper reportMapper;


    @Test
    void saveReport(){
        Report report = Report.builder().id(1L).build();
        ReportDto reportDto = ReportDto.builder().id(1L).build();
        Optional<User> user = Optional.of(User.builder().id(1L).build());
        report.setUser(user.get());
        doReturn(report).when(reportMapper).mapToReport(reportDto);
        doReturn(user).when(reportRepository).findById(1L);
    }
}
