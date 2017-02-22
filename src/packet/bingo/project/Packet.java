package packet.bingo.project;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Packet implements Serializable{
	
	private static final long serialVersionUID = 4894946798145447397L;
	private PacketType protocol;
	private String mainField;
	private String subField;
	
}
