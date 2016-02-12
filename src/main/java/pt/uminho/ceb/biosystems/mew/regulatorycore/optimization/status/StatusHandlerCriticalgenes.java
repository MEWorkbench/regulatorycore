package pt.uminho.ceb.biosystems.mew.regulatorycore.optimization.status;

public class StatusHandlerCriticalgenes  {
	
	
	public float progress ;
	public String status = "0.0%";
	protected int current=0;
	private int maxNumberOfFunctionEvaluations = 0;
	private boolean terminationflag = false;
	
	

	public void processEvaluationEvent(int current) {

						
			//current++;
			float progress = (float)current/(float)maxNumberOfFunctionEvaluations;
			int progressRound = Math.round(progress*100);
			if(progressRound > 100){
				progressRound = 100;
				progress = 1;
			}
			setProgress(progress);
			setStatus("Running: "+progressRound+"%");
		}
	

	synchronized public float getProgress() {
		return progress;
	}
	
	synchronized public void setProgress(float progress) {
		this.progress= progress;
	}   
	
	synchronized public String getStatus(){
		return status;
	}
	
	synchronized public void setStatus(String status){
		this.status = status;
	}
	
	public void setNumberOfFunctionEvaluations(int maxNumberFunctionEvaluations) {
		this.maxNumberOfFunctionEvaluations = maxNumberFunctionEvaluations;
	}

	
	
	

}
