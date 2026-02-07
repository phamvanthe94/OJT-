package com.ra.base_spring_boot.dto.req;

import com.ra.base_spring_boot.model.constants.MovieStatus;
import com.ra.base_spring_boot.model.constants.MovieType;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MovieReq {

    @NotBlank(message = "Title không được để trống")
    @Size(max = 255, message = "Title tối đa 255 ký tự")
    private String title;

    @Size(max = 5000, message = "Descriptions tối đa 5000 ký tự")
    private String descriptions;

    @Size(max = 100, message = "Author tối đa 100 ký tự")
    private String author;

    @Size(max = 255, message = "Trailer tối đa 255 ký tự")
    private String trailer;

    // ✅ enum -> swagger dropdown
    // Với enum MovieType của bạn: swagger có thể hiện _2D/_3D,
    // nhưng client nên gửi "2D"/"3D" (nhờ @JsonCreator/@JsonValue).
    @NotNull(message = "Type không được để trống")
    private MovieType type;

    @NotNull(message = "Duration không được để trống")
    @Min(value = 1, message = "Duration phải >= 1 phút")
    @Max(value = 500, message = "Duration tối đa 500 phút")
    private Integer duration;

    @NotNull(message = "ReleaseDate không được để trống")
    @DateTimeFormat(pattern = "yyyy-MM-dd") // ✅ bind form-data
    private LocalDate releaseDate;

    // ✅ enum -> swagger dropdown
    @NotNull(message = "Status không được để trống")
    private MovieStatus status;

    // danh sách genre id (không thể dropdown trong swagger vì là DB data)
    private Set<@NotNull(message = "GenreId không hợp lệ") Long> genreIds;

    // create bắt buộc/ update optional -> check trong service
    private MultipartFile image;
}
