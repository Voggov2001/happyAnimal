package com.coderiders.happyanimal.controller;

import com.coderiders.happyanimal.model.dto.ReportDto;
import com.coderiders.happyanimal.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reports")
@Tag(name = "report-controller", description = "отчеты (отправляются сотрудниками админам)")
public class ReportController {
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(summary = "Все созданные отчеты")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<ReportDto> getAllReports(Pageable pageable) {
        return reportService.getAllReportsDTO(pageable);
    }

    @Operation(summary = "Создать отчет")
    @PostMapping
    public ResponseEntity<ReportDto> addReport(@Valid @RequestBody ReportDto reportDto, Long userId) {
        var created = reportService.saveReport(reportDto, userId);
        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(url).body(created);
    }

    @Operation(summary = "Отчеты конкретного пользователя")
    @GetMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<ReportDto> getReportsByUserId(@PathVariable Long userId, Pageable pageable) {
        return reportService.getReportDTOByUserId(userId, pageable);
    }
}
