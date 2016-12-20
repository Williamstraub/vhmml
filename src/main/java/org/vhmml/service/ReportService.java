package org.vhmml.service;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.vhmml.dto.Report;

public interface ReportService {
	public Report runReport(Report.Type reportType, Map<String, String> reportParameters, Pageable pageable);
	public String getReportCSV(Report.Type reportType, Map<String, String> reportParameters, Pageable pageable);
}
