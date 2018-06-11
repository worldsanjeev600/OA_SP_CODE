package com.oneassist.serviceplatform.commons.mastercache;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.oneassist.serviceplatform.commons.cache.base.InMemoryCache;
import com.oneassist.serviceplatform.commons.workflowmanager.IWorkflowManager;

@Configuration
@Component("taskListForAllDeploymentCache")
public class TaskListForAllDeploymentCache extends InMemoryCache<List<String>>{
	
	@Autowired
    protected IWorkflowManager workflowManager;
	
	private Map<String, List<String>> taskVersionNameMap;
	
	@Override
	protected List<String> getFromDB(String key) {
		// TODO Auto-generated method stub
		taskVersionNameMap = getAllFromDB();
		return taskVersionNameMap.get(key);
	}

	@Override
	protected Map<String, List<String>> getAllFromDB() {
		// TODO Auto-generated method stub
		taskVersionNameMap = workflowManager.getAllTaskNameForAllDeployments();
		return taskVersionNameMap;
	}

}
