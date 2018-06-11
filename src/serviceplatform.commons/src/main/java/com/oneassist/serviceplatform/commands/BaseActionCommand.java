package com.oneassist.serviceplatform.commands;

import com.oneassist.serviceplatform.commands.dtos.*;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;

public abstract class BaseActionCommand<TInput, TResult> {
	
	protected abstract CommandResult<TResult> canExecuteCommand(CommandInput<TInput> commandInput) throws BusinessServiceException;
	
	protected abstract CommandResult<TResult> executeCommand(CommandInput<TInput> commandInput) throws Exception;
	
	public CommandResult<TResult> execute(CommandInput<TInput> commandInput) throws Exception {
		CommandResult<TResult> commandResult = this.canExecuteCommand(commandInput);
		
		if (commandResult.getCanExecute()) {
			return this.executeCommand(commandInput);
		}

		return commandResult;
	}
}