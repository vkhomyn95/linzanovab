package com.backend.linzanova.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ItemDTO {
    private List<DropDTO> drops;
    private List<SpecialDTO> offers;
    private List<LensOrderDTO> lenses;
    private List<SolutionOrderDTO> solutions;
}
