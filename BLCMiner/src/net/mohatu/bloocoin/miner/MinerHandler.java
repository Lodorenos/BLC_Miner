/*
 * Copyright (C) 2013 Mohatu.net
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 * @author Mohatu
 * @version 2.9
 */

package net.mohatu.bloocoin.miner;

public class MinerHandler implements Runnable {
	@Override
	public void run() {
		for (int i = 0; i < Main.getThreads(); i++) {
			Thread miner = new Thread(new Miner(8));
			miner.start();
			System.out.println("Thread " + (i + 1) + " started.");
		}
	}
}
