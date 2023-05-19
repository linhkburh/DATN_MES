package cic.h2h.form;

import entity.WorkOrder;
import frwk.form.SearchForm;

public class WorkerForm extends SearchForm<WorkOrder> {

	WorkOrder workOrder = new WorkOrder();

	public WorkOrder getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}

	@Override
	public WorkOrder getModel() {
		// TODO Auto-generated method stub
		return null;
	}

}
