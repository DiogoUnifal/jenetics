/*
 * Java Genetic Algorithm Library (@!identifier!@).
 * Copyright (c) @!year!@ Franz Wilhelmstötter
 *  
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Author:
 *     Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
 *     
 */
package org.jenetics.util;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicLong;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javolution.context.ConcurrentContext;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
public class ForkJoinContextTest {

	static {
		ForkJoinContext.setForkkJoinPool(new ForkJoinPool(1));
	}
	
	@Test(dataProvider = "levels")
	public void execute(final Integer level) {
		try {
			Concurrency.setContext(ForkJoinContext.class);
			Assert.assertEquals(_execute(level), level + 1);
		} finally {
			Concurrency.setDefaultContext();
		}
	}
	
	private long _execute(final Integer level) {
		final AtomicLong counter = new AtomicLong(1);
		
		ConcurrentContext.enter();
		try {
			ConcurrentContext.execute(new Runnable() {
				@Override public void run() {
					if (level == 0) {
						System.out.println("READY");
					} else {
						counter.addAndGet(_execute(level - 1));
					}	
				}
			});
			ConcurrentContext.execute(new Runnable() {
				@Override
				public void run() {
					try {
						exec();
						Thread.sleep(5);
					} catch (InterruptedException e) {
						assert(false) : e.toString();
					}
				}
			});
		} finally {
			ConcurrentContext.exit();
		}
		
		return counter.longValue();
	}
	
	private static void exec() {
		ConcurrentContext.enter();
		try {
			for (int i = 0; i < 5; ++i) {
				ConcurrentContext.execute(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(2);
						} catch (InterruptedException e) {
							assert(false) : e.toString();
						}
					}
				});
				}
		} finally {
			ConcurrentContext.exit();
		}		
	}
	
	@DataProvider(name = "levels")
	public Object[][] levels() {
		return new Object[][] {
			{ 1 }, { 2 }, { 5 }, { 7 }, { 10 }, { 15 }, { 21 }, { 50 }, { 100 }
		};
	}

	@Test
	public void deadLock() {

	}
	
	
}



