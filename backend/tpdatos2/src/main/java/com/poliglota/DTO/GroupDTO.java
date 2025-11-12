package com.poliglota.DTO;

import lombok.Data;
import java.util.List;

@Data
public class GroupDTO {
	private String id;
	private String name;
	private List<Long> memberIds;
}
