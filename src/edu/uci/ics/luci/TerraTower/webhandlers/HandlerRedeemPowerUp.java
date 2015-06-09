/*
	Copyright 2014-2015
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
package edu.uci.ics.luci.TerraTower.webhandlers;


import java.util.Set;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.TerraTower.TTEventWrapperQueuer;
import edu.uci.ics.luci.TerraTower.events.TTEventRedeemPowerUp;
import edu.uci.ics.luci.TerraTower.events.TTEventType;
import edu.uci.ics.luci.utility.webserver.handlers.HandlerAbstract;
import edu.uci.ics.luci.utility.webserver.input.request.Request;
import edu.uci.ics.luci.utility.webserver.output.channel.Output;
import edu.uci.ics.luci.utility.webserver.output.response.Response;

public class HandlerRedeemPowerUp extends HandlerAbstractPlayer {
	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(HandlerRedeemPowerUp.class);
		}
		return log;
	}

	
	public HandlerRedeemPowerUp(TTEventWrapperQueuer eventPublisher) {
		super(eventPublisher);
	}

	@Override
	public HandlerAbstract copy() {
		return new HandlerRedeemPowerUp(this.getEventPublisher());
	}
	
	/**
	 * @param parameters a map of key and value that was passed through the REST request
	 * @return a pair where the first element is the content type and the bytes are the output bytes to send back
	 */
	@Override
	public Response handle(Request request, Output o) {
		
		JSONObject ret = new JSONObject();
		
		Response response = o.makeOutputChannelResponse();
		
		JSONArray errors = new JSONArray();
		
		errors.addAll(getWorldParameters(request.getCommand(),request.getParameters()));
		errors.addAll(getPlayerParameters(request.getCommand(),request.getParameters()));
		
		Set<String> _code = request.getParameters().get("code");
		String code = null;
		if((_code == null) ||((code = _code.iterator().next()) == null)){
			errors.add("Problem handling "+request.getCommand()+": code was null");
		}
		
		if(errors.size() != 0){
			ret.put("error", "true");
			ret.put("errors", errors);
		}
		else{
			TTEventRedeemPowerUp tt = new TTEventRedeemPowerUp(getWorldName(),getWorldPassword(),getPlayerName(),getPlayerPassword(),code);
			ret = this.queueEvent(TTEventType.REDEEM_POWER_UP, tt);
		}
		
		response.setStatus(Response.Status.OK);
		response.setDataType(Response.DataType.JSON);
		response.setBody(wrapCallback(request.getParameters(),ret.toString()));
		
		return response;
	}
}


