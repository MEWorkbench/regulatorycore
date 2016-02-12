package pt.uminho.ceb.biosystems.mew.regulatorycore.container.io.readers.regulatorynetwork.auxiliar;

public class RegModelInfoContainer {
	private int GPRLinkColumn;
	private int TFsLinkColumn;
	private int GeneNamesColumn=-1;
	private int totalcolumns;
	


	private String delimiter=null;
	
	
	public RegModelInfoContainer(int GPRLinkColumn, int TFsLinkColumn, int GeneNamesColumn, int totalcolumns, String delimiter) {
		this.GPRLinkColumn = GPRLinkColumn;
		this.TFsLinkColumn = TFsLinkColumn;
		this.GeneNamesColumn = GeneNamesColumn;
		this.totalcolumns=totalcolumns;
		this.delimiter = delimiter;
	}
	
	public RegModelInfoContainer(int GPRLinkColumn, int TFsLinkColumn, int GeneNamesColumn) {
		this.GPRLinkColumn = GPRLinkColumn;
		this.TFsLinkColumn = TFsLinkColumn;
		this.GeneNamesColumn = GeneNamesColumn;
	}


	public int getGPRLinkColumn() {
		return GPRLinkColumn;
	}


	public int getTFsLinkColumn() {
		return TFsLinkColumn;
	}


	public int getGeneNamesColumn() {
		return GeneNamesColumn;
	} 
	
    public int getTotalcolumns() {
		return totalcolumns;
	}

	public String getDelimiter() {
		return delimiter;
	}
	
	
	
	

}
