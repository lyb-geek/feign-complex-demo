package com.github.geek.lyb.feign.dto;

import com.github.geek.lyb.feign.annotation.FieldAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPageDTO {

    @FieldAlias(isPojo = true)
    private UserDTO userDTO;

    private int pageSize;

    @FieldAlias("pageIndex")
    private int pageNo;
}
