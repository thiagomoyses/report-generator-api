package com.tsm.report_generator_api.facade;

import com.tsm.report_generator_api.exception.NotFoundException;
import com.tsm.report_generator_api.service.student.FindAllStudentsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportFacadeImplTest {

    @Mock
    private FindAllStudentsService findAllStudentsService;

    private ReportFacadeImpl reportFacade;

    @BeforeEach
    void setUp() {
        reportFacade = new ReportFacadeImpl(findAllStudentsService);
    }

    @Test
    void shouldDelegateToFindAllStudentsServiceWhenReportNameIsFindAllStudents(){
        // Given
        byte[] expected = new byte[]{1, 2, 3};
        when(findAllStudentsService.generateReport(Map.of(), "PDF")).thenReturn(expected);

        // when
        byte[] result = reportFacade.generateReport("findallstudents", Map.of(), "PDF");

        // then
        assertThat(result).isEqualTo(expected);
        verify(findAllStudentsService).generateReport(Map.of(), "PDF");
    }

    @Test
    void shouldThrowNotFoundExceptionWhenReportDoesNotExistInMap() {
        assertThatThrownBy(() -> reportFacade.generateReport("unknown", Map.of(), "PDF"))
                .isInstanceOf(NotFoundException.class);

        verifyNoInteractions(findAllStudentsService);
    }

    @Test
    void shouldBeCaseSensitiveOnReportName() {
        assertThatThrownBy(() ->
                reportFacade.generateReport("FindAllStudents", Map.of(), "PDF")
        ).isInstanceOf(NotFoundException.class);
    }
}
