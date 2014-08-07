package guifx.components;


public class IntegerValueFilter extends QueryFilter {
	public final IntegerConstraintField	field; 
	
	public IntegerValueFilter(String label, String fieldName) {
		super(label,fieldName);
		field = new IntegerConstraintField();
	}

	@Override
	public String getQuery() {
		return fieldName + "=" + field.getValue();
	}
}
