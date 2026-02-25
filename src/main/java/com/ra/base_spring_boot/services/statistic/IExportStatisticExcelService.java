package com.ra.base_spring_boot.services.statistic;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;

public interface IExportStatisticExcelService {
    ByteArrayInputStream exportStatisticExcel(
            LocalDateTime from,
            LocalDateTime to
    );
}
