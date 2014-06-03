/*
	Copyright 2014
		University of California, Irvine (c/o Donald J. Patterson)
*/
/*
	This file is part of the Laboratory for Ubiquitous Computing java TerraTower game, i.e. "TerraTower"

    TerraTower is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Utilities is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Utilities.  If not, see <http://www.gnu.org/licenses/>.
*/
package edu.uci.ics.luci.TerraTower;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONObject;
import edu.uci.ics.luci.TerraTower.events.TTEvent;
import edu.uci.ics.luci.TerraTower.events.TTEventCreatePlayer;
import edu.uci.ics.luci.TerraTower.events.TTEventCreateTerritory;
import edu.uci.ics.luci.TerraTower.events.TTEventCreateWorld;
import edu.uci.ics.luci.TerraTower.events.TTEventPlaceTower;
import edu.uci.ics.luci.TerraTower.events.TTEventStepTowerTerritoryGrowth;
import edu.uci.ics.luci.TerraTower.events.TTEventType;
import edu.uci.ics.luci.TerraTower.events.TTEventVoid;
import edu.uci.ics.luci.TerraTower.events.handlers.TTEventHandler;
import edu.uci.ics.luci.TerraTower.events.handlers.TTEventHandlerCreatePlayer;
import edu.uci.ics.luci.TerraTower.events.handlers.TTEventHandlerCreateTerritory;
import edu.uci.ics.luci.TerraTower.events.handlers.TTEventHandlerCreateWorld;
import edu.uci.ics.luci.TerraTower.events.handlers.TTEventHandlerPlaceTower;
import edu.uci.ics.luci.TerraTower.events.handlers.TTEventHandlerStepTowerTeritoryGrowth;
import edu.uci.ics.luci.TerraTower.events.handlers.TTEventHandlerVoid;

/**
 * Setting the eventType and the event is required
 * @author djp3
 *
 */
public class TTEventWrapper {
	
	/* The basic data encapsulated by the wrapper */
	private long timestamp;		//Official time of the event
	private TTEventType eventType;
	private TTEvent event;
	private transient TTEventHandler handler;
	private transient List<TTEventHandlerResultListener> resultListeners;
	
	/* Getters and Setters */
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public TTEventType getEventType() {
		return eventType;
	}

	public void setEventType(TTEventType eventType) {
		this.eventType = eventType;
	}
	
	public TTEvent getEvent(){
		return event;
	}
	
	public void setEvent(TTEvent event){
		this.event = event;
		resetEventHandler();
	}
	
	public TTEventHandler getHandler(){
		return handler;
	}
	
	private void setHandler(TTEventHandler handler){
		this.handler = handler;
	}
	
	/** This is only helpful for testing
	 * 
	 */
	public void resetEvent(){
		switch(this.getEventType()){
		case CREATE_WORLD: this.setEvent(new TTEventCreateWorld((String)null, (String)null));
			break;
		case CREATE_TERRITORY: this.setEvent(new TTEventCreateTerritory((String)null, (String)null, null, null, null, null, null, null));
			break;
		case CREATE_PLAYER: this.setEvent(new TTEventCreatePlayer((String)null, (String)null, null, null));
			break;
		case PLACE_TOWER: this.setEvent(new TTEventPlaceTower((String)null, (String)null, null, null, null, null, null));
			break;
		case STEP_TOWER_TERRITORY_GROWTH: this.setEvent(new TTEventStepTowerTerritoryGrowth((String)null,(String)null));
			break;
		case VOID: this.setEvent(new TTEventVoid());
			break;
		default: this.setEvent(null);
			break;
		
		}
	}
	public void resetEventHandler(){
		switch(this.getEventType()){
		case CREATE_WORLD: this.setHandler(new TTEventHandlerCreateWorld());
			break;
		case CREATE_TERRITORY: this.setHandler(new TTEventHandlerCreateTerritory());
			break;
		case CREATE_PLAYER: this.setHandler(new TTEventHandlerCreatePlayer());
			break;
		case PLACE_TOWER: this.setHandler(new TTEventHandlerPlaceTower());
			break;
		case STEP_TOWER_TERRITORY_GROWTH: this.setHandler(new TTEventHandlerStepTowerTeritoryGrowth());
			break;
		case VOID: this.setHandler(new TTEventHandlerVoid());
			break;
		default: this.setHandler(null);
			break;
		
		}
	}
	
	public void checkConsistency(){
		boolean problem = false;
		switch(this.getEventType()){
		case CREATE_TERRITORY: problem = (!(this.getEvent() instanceof TTEventCreateTerritory));
							   problem |= (!(this.getHandler() instanceof TTEventHandlerCreateTerritory));
			break;
		case CREATE_PLAYER: problem = (!(this.getEvent() instanceof TTEventCreatePlayer));
							   problem |= (!(this.getHandler() instanceof TTEventHandlerCreatePlayer));
			break;
		case CREATE_WORLD: problem = (!(this.getEvent() instanceof TTEventCreateWorld));
							   problem |= (!(this.getHandler() instanceof TTEventHandlerCreateWorld));
			break;
		case PLACE_TOWER: problem = (!(this.getEvent() instanceof TTEventPlaceTower));
							   problem |= (!(this.getHandler() instanceof TTEventHandlerPlaceTower));
			break;
		case VOID: problem = (!(this.getEvent() instanceof TTEventVoid));
							   problem |= (!(this.getHandler() instanceof TTEventHandlerVoid));
			break;
		case STEP_TOWER_TERRITORY_GROWTH: problem = (!(this.getEvent() instanceof TTEventStepTowerTerritoryGrowth));
							   problem |= (!(this.getHandler() instanceof TTEventHandlerStepTowerTeritoryGrowth));
			break;
		default:
			problem = true;
			break;
		}
		if(problem){
			throw new IllegalArgumentException("EventType:"+this.getEventType()+" is inconsistent with Event:"+this.getEvent().getClass().getCanonicalName());
		}
	}
	
	public List<TTEventHandlerResultListener>getResultListeners(){
		return this.resultListeners;
	}
	
	public void addResultListener(TTEventHandlerResultListener rl){
		if(rl != null){
			resultListeners.add(rl);
		}
	}
	
	public void setResultListeners(List<TTEventHandlerResultListener> rl){
		this.resultListeners = rl;
	}
	
	
	TTEventWrapper(TTEventType eventType,TTEvent event,TTEventHandlerResultListener resultListener){
		this(System.currentTimeMillis(),eventType,event,resultListener);
	}

	
	TTEventWrapper(long eventTime,TTEventType eventType,TTEvent event,TTEventHandlerResultListener resultListener){
		this.setTimestamp(eventTime);
		
		if(eventType == null){
			throw new IllegalArgumentException("eventType can't be null");
		}
		this.setEventType(eventType);
		
		//Event can be null because it has to be initialized for disruptor
		//before an event exists
		this.setEvent(event);
		
		this.setResultListeners(new ArrayList<TTEventHandlerResultListener>());
		this.addResultListener(resultListener);
	}
	
	
	TTEventWrapper(TTEventType eventType,TTEvent event,List<TTEventHandlerResultListener> resultListeners){
		this(System.currentTimeMillis(),eventType,event,resultListeners);
	}

	
	TTEventWrapper(long eventTime,TTEventType eventType,TTEvent event,List<TTEventHandlerResultListener> resultListeners){
		
		this.setTimestamp(eventTime);
		if(eventType == null){
			throw new IllegalArgumentException("eventType can't be null");
		}
		this.setEventType(eventType);
		
		//Event can be null because it has to be initialized for disruptor
		//before an event exists
		this.setEvent(event);
		
		if(resultListeners == null){
			this.setResultListeners(new ArrayList<TTEventHandlerResultListener>());
		}
		else{
			this.setResultListeners(resultListeners);
		}
	}
	
	void set(TTEventWrapper ttEventWrapper){
		this.setTimestamp(ttEventWrapper.getTimestamp());
		this.setEventType(ttEventWrapper.getEventType());
		this.setEvent(ttEventWrapper.getEvent());
		this.setHandler(ttEventWrapper.getHandler());
		this.getResultListeners().clear();
		this.getResultListeners().addAll(ttEventWrapper.getResultListeners());
		checkConsistency();
	}
	
	@Override
	public String toString(){
		String localEventType = "";
		String localEvent = "";
		if(this.getEventType() == null){
			localEventType = "null";
		}
		if(this.getEvent() == null){
			localEvent = "null";
		}
		return(localEventType+":"+localEvent);
	}
	
	public JSONObject toJSON(){
		JSONObject ret = new JSONObject();
		ret.put("timestamp",""+getTimestamp());
		ret.put("eventType",getEventType().toString());
		ret.put("event", getEvent().toJSON());
		return ret;
	}
	
	static public TTEventWrapper fromJSON(JSONObject in){
		long eventTime = Long.parseLong((String) in.get("timestamp"));
		TTEventType eventType = TTEventType.fromString((String) in.get("eventType"));
		TTEvent event;
		switch(eventType){
			case VOID: event = TTEventVoid.fromJSON((JSONObject)in.get("event"));
				break;
			case CREATE_WORLD: event = TTEventCreateWorld.fromJSON((JSONObject)in.get("event"));
				break;
			case CREATE_TERRITORY: event = TTEventCreateTerritory.fromJSON((JSONObject)in.get("event"));
				break;
			case CREATE_PLAYER: event = TTEventCreatePlayer.fromJSON((JSONObject)in.get("event"));
				break;
			case PLACE_TOWER: event = TTEventPlaceTower.fromJSON((JSONObject)in.get("event"));
				break;
			default:event = null;
				break;
		}
		TTEventWrapper ret = new TTEventWrapper(eventTime,eventType,event,(TTEventHandlerResultListener)null);
		return ret;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		result = prime * result
				+ ((eventType == null) ? 0 : eventType.hashCode());
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TTEventWrapper)) {
			return false;
		}
		TTEventWrapper other = (TTEventWrapper) obj;
		if (event == null) {
			if (other.event != null) {
				return false;
			}
		} else if (!event.equals(other.event)) {
			return false;
		}
		if (eventType != other.eventType) {
			return false;
		}
		if (timestamp != other.timestamp) {
			return false;
		}
		return true;
	}




}
