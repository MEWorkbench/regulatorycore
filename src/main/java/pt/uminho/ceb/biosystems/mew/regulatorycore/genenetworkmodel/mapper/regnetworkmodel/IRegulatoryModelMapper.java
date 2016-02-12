package pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.mapper.regnetworkmodel;

import pt.uminho.ceb.biosystems.mew.regulatorycore.genenetworkmodel.networkmodel.IRegulatoryModel;



public interface IRegulatoryModelMapper extends IRegulatoryModel{

	void setModel(IRegulatoryModel model);
	IRegulatoryModel getModel();
	IRegulatoryDecoder getDecoder();
	void setDecoder(IRegulatoryDecoder decoder);
	IRegulatoryOverrideModel getOverrideModel();
	void setOverrideModel(IRegulatoryOverrideModel overrideModel);
}
