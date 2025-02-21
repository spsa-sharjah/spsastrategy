package com.spsa.strategy.model;
import jakarta.persistence.*;

@Entity
@Table(name = "goalstatus")
public class GoalStatus {

    @Id
    @Column(name = "code", nullable = false, length = 200)
    private String code;

    @Column(name = "nameen", nullable = false, length = 200)
    private String nameen;

    @Column(name = "namear", length = 200)
    private String namear;

    @Lob
    @Column(name = "description")
    private String description;

    public GoalStatus() {}

    public GoalStatus(String nameen, String namear, String description) {
        this.nameen = nameen;
        this.namear = namear;
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public String getNameen() {
		return nameen;
	}

	public void setNameen(String nameen) {
		this.nameen = nameen;
	}

	public String getNamear() {
		return namear;
	}

	public void setNamear(String namear) {
		this.namear = namear;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
