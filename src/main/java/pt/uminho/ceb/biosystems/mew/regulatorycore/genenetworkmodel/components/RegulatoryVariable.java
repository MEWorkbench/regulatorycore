package pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.components;

import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.UnsuportedVariableException;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.components.VariableCI;

public class RegulatoryVariable extends VariableCI {
	
	private static final long serialVersionUID = 1L;
	
	protected boolean state = false;
	
	
	
	public RegulatoryVariable(String id, String name){
		super(id,name);
	}

	
	public void setNewState(boolean val){
		this.state = val;
	}
	
	public boolean getStateOfVariable(){
		return state;
	}

    @Override
	public Object clone() throws CloneNotSupportedException{
		RegulatoryVariable clone = (RegulatoryVariable)super.clone();
		clone.setId(this.id);
		clone.setName(this.name);
		try {
			clone.setTypeVar(this.type);
		} catch (UnsuportedVariableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clone.setNewState(this.state);
		
		return clone;
	}
	

}
