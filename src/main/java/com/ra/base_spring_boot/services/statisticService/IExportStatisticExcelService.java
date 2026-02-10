package com.ra.base_spring_boot.services.statisticService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;

public interface IExportStatisticExcelService {
    ByteArrayInputStream exportStatisticExcel(
            LocalDateTime from,
            LocalDateTime to
    );
}
