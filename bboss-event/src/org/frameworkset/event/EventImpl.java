/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package org.frameworkset.event;

import org.frameworkset.remote.EventUtils;
import org.jgroups.Address;



/**
 * 事件接口实现
 *
 * @author biaoping.yin
 * @version 1.0
 */
public final class EventImpl<T> implements Event<T> {
	/**
	 *  
	 */
	private static final long serialVersionUID = -1242731152082695701L;
	T source;
	EventType type ;
	/**
	 * 事件消息广播的目的地址，只有eventBroadcastType为远程传播(Event.REMOTE)
	 * 本地远程传播 (Event.REMOTELOCAL)两种类型时，才可以指定target属性
	 * 如果target为null，那么事件将被广播到所有的远程节点上面的监听器，否则只广播到target
	 * 指定的目的地址对应的远程监听器上面，如果对应的target不存在，那么直接丢弃这个事件消息
	 */
	EventTarget target = null;
	
	/**
	 * 如果target不为空，则eventBroadcastType无效，只有在target为空的情况下eventBroadcastType才有效
	 * 消息传播类型：
	 * 本地传播(Event.LOCAL)，事件只在本地广播
	 * 远程传播(Event.REMOTE) 事件只在集群中的其他节点中广播（启用远程事件时才有效）
	 * 本地远程传播 (Event.REMOTELOCAL) 事件在集群中所有节点广播 默认值（启用远程事件才有效）
	 */
	int eventBroadcastType = Event.REMOTELOCAL;
	boolean synchronize = false;
	
	static int defaultEventBroadcastType = Event.REMOTELOCAL;

	public EventImpl(T source,EventType type )
	{
		this(source,type ,Event.REMOTELOCAL);
	}
	
//	public EventImpl(T source,EventType type ,EventTarget target )
//	{
//		this.source = source;
//		this.type = type;
//		this.target = target;
//		if(eventBroadcastType == Event.LOCAL ||
//			eventBroadcastType == Event.REMOTE ||
//			eventBroadcastType == Event.REMOTELOCAL)
//		{
//			this.eventBroadcastType = eventBroadcastType;
//		}
//		else
//		{
//			System.out.println("Incorrect EventBroadcastType:" + eventBroadcastType + ", the Event.REMOTELOCAL will been used.");
//			this.eventBroadcastType = Event.REMOTELOCAL;
//		}
//		
//	}
	
	public EventImpl(T source,EventType type ,EventTarget target)
	{
		this.source = source;
		this.type = type;
		this.target = target;
		
		if(	eventBroadcastType == Event.REMOTE ||
				eventBroadcastType == Event.REMOTELOCAL)
		{
			Address address =  EventUtils.getLocalAddress();
			if(address != null)
				this.sourceAddress = address ;
		}
	}
	
	public EventImpl(T source,EventType type ,int eventBroadcastType)
	{
		this.source = source;
		this.type = type;
		if(eventBroadcastType == Event.LOCAL ||
			eventBroadcastType == Event.REMOTE ||
			eventBroadcastType == Event.REMOTELOCAL)
		{
			this.eventBroadcastType = eventBroadcastType;
		}
		else
		{
			throw new java.lang.IllegalArgumentException("event Broadcast Type ["+eventBroadcastType+"] should be Event.LOCAL or Event.REMOTE or Event.REMOTELOCAL.");
		}
		if(	eventBroadcastType == Event.REMOTE ||
				eventBroadcastType == Event.REMOTELOCAL)
		{
			Address address =  EventUtils.getLocalAddress();
			if(address != null)
				this.sourceAddress = address;
		}
	}
	private Address sourceAddress;
	public EventImpl()
	{
		
	} 
	/**
	 *  Description:
	 * @return
	 * @see com.chinacreator.security.authorization.ACLEvent#getSource()
	 */
	public T getSource() {

		return source;
	}

	/**
	 *  Description:
	 * @return
	 * @see com.chinacreator.security.authorization.ACLEvent#getType()
	 */
	public EventType getType() {

		return type;
	}
	public boolean isSynchronize() {

		return this.synchronize;
	}
	
	public void setSynchronize(boolean synchronize)
	{
		this.synchronize = synchronize;
	}
	public void setSource(T source) {
		this.source = source;
	}
	public void setType(EventType type) {
		this.type = type;
	}
 
	
	public boolean isLocal() {
		
		return this.eventBroadcastType == Event.LOCAL;
	}
	public boolean isRemote() {
		
		return this.eventBroadcastType == Event.REMOTE;
	}
	public boolean isRemoteLocal() {
		return this.eventBroadcastType == Event.REMOTELOCAL;
	}

	public int getEventBroadcastType() {
		return eventBroadcastType;
	}

	public EventTarget getEventTarget() {
		
		return this.target;
	}
	/**
	 * 返回事件源节点地址
	 */
	public Address getSourceAddress()
	{
		return this.sourceAddress;
	}
	 

}
