package com.coderiders.happyanimal.service;

import com.coderiders.happyanimal.exceptions.NotFoundException;
import com.coderiders.happyanimal.mapper.ReportMapper;
import com.coderiders.happyanimal.model.Report;
import com.coderiders.happyanimal.model.User;
import com.coderiders.happyanimal.model.dto.ReportDto;
import com.coderiders.happyanimal.repository.ReportRepository;
import com.coderiders.happyanimal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ReportMapper reportMapper;
    private static final String ERROR_MESSAGE_NOT_FOUND_REPORT = "Отчет не найден";
    private static final String ERROR_MESSAGE_NOT_FOUND_USER = "Пользователь не найден";

    @Autowired
    public ReportService(ReportRepository reportRepository, UserRepository userRepository, ReportMapper reportMapper) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.reportMapper = reportMapper;
    }

    @Transactional
    public ReportDto saveReport(ReportDto reportDto, Long userId) {
        Report report = reportMapper.mapToReport(reportDto);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(ERROR_MESSAGE_NOT_FOUND_REPORT));
        report.setUser(user);
        return reportMapper.mapToDto(reportRepository.save(report));
    }

    @Transactional
    public Page<ReportDto> getAllReportsDTO(Pageable pageable, Long userId) {
        if (Optional.ofNullable(userId).isPresent()){
            return getReportDTOByUserId(pageable, userId);
        }
        Page<Report> allReports = reportRepository.findAll(pageable);
        if (allReports.isEmpty()) {
            throw new NotFoundException(ERROR_MESSAGE_NOT_FOUND_REPORT);
        }
        return allReports.map(reportMapper::mapToDto);
    }

    @Transactional
    public Page<ReportDto> getReportDTOByUserId(Pageable pageable, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(ERROR_MESSAGE_NOT_FOUND_USER));
        Page<Report> reportList = reportRepository.findAllByUser(user, pageable);
        if (reportList.isEmpty()) {
            throw new NotFoundException(ERROR_MESSAGE_NOT_FOUND_REPORT);
        }
        return reportList.map(reportMapper::mapToDto);
    }
}