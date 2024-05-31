package com.wqa.cems.model.dto.user;

import com.wqa.cems.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserSearchRequest extends PageRequest implements Serializable {

    private String field;
    private String fieldValue;

    private static final long serialVersionUID = 1L;
}
