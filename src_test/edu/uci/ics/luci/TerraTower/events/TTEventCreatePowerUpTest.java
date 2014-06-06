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
package edu.uci.ics.luci.TerraTower.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.TerraTower.gameElements.PowerUp;

public class TTEventCreatePowerUpTest {
	static final String worldName = "name";
	static final String worldPassword = "password";
	static final String playerName = "name";
	static final String playerPassword = "password";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	

	@Test
	public void testEquals() {
		
		PowerUp pup = new PowerUp("code",-1000L,-1000L,-1000L,false);
		TTEventCreatePowerUp t1 = new TTEventCreatePowerUp(worldName,worldPassword,pup);
		assertEquals(t1,t1);
		assertTrue(!t1.equals(null));
		assertTrue(!t1.equals("string"));
		
		TTEventCreatePowerUp t2 = new TTEventCreatePowerUp(worldName,worldPassword,pup);
		TTEventCreatePowerUp t3 = new TTEventCreatePowerUp(worldName,worldPassword,pup);
		assertTrue(t2.equals(t3));
		assertTrue(t2.hashCode()==t3.hashCode());
		
	}

	@Test
	public void testBasic() {
		PowerUp pup = new PowerUp("code",-1000L,-1000L,-1000L,false);
		new TTEventCreatePowerUp(worldName,worldPassword,pup);
	}
	
	@Test
	public void testJSON() {
		PowerUp pup = new PowerUp("code",-1000L,-1000L,-1000L,false);
		TTEventCreatePowerUp t1 = new TTEventCreatePowerUp(worldName,worldPassword,pup);
		
		assertEquals(t1,TTEventCreatePowerUp.fromJSON(t1.toJSON()));
	}

}
