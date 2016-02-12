package pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.components;

import java.io.Serializable;
import java.util.ArrayList;

import pt.uminho.ceb.biosystems.mew.utilities.grammar.syntaxtree.AbstractSyntaxTree;
import pt.uminho.ceb.biosystems.mew.utilities.grammar.syntaxtree.AbstractSyntaxTreeNode;
import pt.uminho.ceb.biosystems.mew.utilities.grammar.syntaxtree.TreeUtils;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.DataTypeEnum;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.IValue;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.parser.ParseException;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.parser.ParserSingleton;



public class RegulatoryRule implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String ruleId;
	private String rule;
	protected AbstractSyntaxTree<DataTypeEnum, IValue> booleanRule;
	
	public RegulatoryRule(String ruleId,String rule) throws ParseException{
		this.ruleId = ruleId;
		this.rule = rule;
		
		if(rule == null || rule.equals("")){
			this.booleanRule = new AbstractSyntaxTree<DataTypeEnum, IValue>();
		}else{
		
			AbstractSyntaxTreeNode<DataTypeEnum, IValue> ast;
			
			ast = ParserSingleton.boolleanParserString(rule);
			
			this.booleanRule = new AbstractSyntaxTree<DataTypeEnum, IValue>(ast);
		}
	}
	
	public RegulatoryRule(String ruleId,String rule, AbstractSyntaxTree<DataTypeEnum, IValue> booleanrule ) throws ParseException{
		this.ruleId = ruleId;
		this.rule = rule;
		this.booleanRule = booleanrule;
		
	}
	
	public void setRule(String rule) throws ParseException{
		this.rule= rule;
		if(rule == null || rule.equals("")){
			this.booleanRule = new AbstractSyntaxTree<DataTypeEnum, IValue>();
		}else{
		
			AbstractSyntaxTreeNode<DataTypeEnum, IValue> ast;
			
			ast = ParserSingleton.boolleanParserString(rule);
			
			this.booleanRule = new AbstractSyntaxTree<DataTypeEnum, IValue>(ast);
		}
	}

	public String getRuleId() {
		return ruleId;
	}

	public String getRule() {
		return rule;
	}

	public AbstractSyntaxTree<DataTypeEnum, IValue> getBooleanRule() {
		return booleanRule;
	}
	
	public ArrayList<String> getVariables(){
		return TreeUtils.withdrawVariablesInRule(booleanRule);
	}
	
	
	public RegulatoryRule copy() throws ParseException{
		
		return new RegulatoryRule(this.ruleId, this.rule, this.booleanRule.copy());
	}


}
