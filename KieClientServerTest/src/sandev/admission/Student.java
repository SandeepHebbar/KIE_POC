package sandev.admission;

/**
 * This class was automatically generated by the data modeler tool.
 */

public class Student implements java.io.Serializable {

	static final long serialVersionUID = 1L;

	@org.kie.api.definition.type.Label(value = "name")
	private java.lang.String name;
	@org.kie.api.definition.type.Label(value = "gpa")
	private java.lang.Float gpa;
	@org.kie.api.definition.type.Label(value = "eligible")
	private java.lang.Boolean eligible;

	public Student() {
	}

	public java.lang.String getName() {
		return this.name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.Float getGpa() {
		return this.gpa;
	}

	public void setGpa(java.lang.Float gpa) {
		this.gpa = gpa;
	}

	public java.lang.Boolean getEligible() {
		return this.eligible;
	}

	public void setEligible(java.lang.Boolean eligible) {
		this.eligible = eligible;
	}

	public Student(java.lang.String name, java.lang.Float gpa, java.lang.Boolean eligible) {
		this.name = name;
		this.gpa = gpa;
		this.eligible = eligible;
	}

}