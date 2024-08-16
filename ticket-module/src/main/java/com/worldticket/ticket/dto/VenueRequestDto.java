package com.worldticket.ticket.dto;

import com.worldticket.ticket.domain.Venue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VenueRequestDto {
    @NotBlank(message = "장소명을 입력해주세요")
    @Size(min = 5, max = 50, message = "장소명은 5자 이상 20자 이하로 입력해주세요")
    private String venueName;

    @NotBlank(message = "주소를 입력해주세요")
    private String location;

    @NotNull(message = "수용량을 입력해주세요")
    @Min(value = 1, message = "수용량은 1이상이어야 합니다.")
    private Integer capacity;

    public static Venue of(VenueRequestDto venueRequestDto) {
        return new Venue(venueRequestDto.getVenueName(), venueRequestDto.getLocation(), venueRequestDto.getCapacity());
    }
}
