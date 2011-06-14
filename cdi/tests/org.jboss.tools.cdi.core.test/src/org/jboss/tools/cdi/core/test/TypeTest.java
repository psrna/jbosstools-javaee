package org.jboss.tools.cdi.core.test;

import java.util.ConcurrentModificationException;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.jboss.tools.cdi.core.IParametedType;
import org.jboss.tools.cdi.internal.core.impl.ParametedType;
import org.jboss.tools.cdi.internal.core.impl.definition.ParametedTypeFactory;
import org.jboss.tools.test.util.JobUtils;
import org.jboss.tools.test.util.ResourcesUtils;

import junit.framework.TestCase;

public class TypeTest extends TestCase {
	IProject project = null;

	public TypeTest() {}
	
	@Override
	protected void setUp() throws Exception {
		project = ResourcesUtils.importProject(DependentProjectsTestSetup.PLUGIN_ID, "/projects/TypeTest");
		project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		JobUtils.waitForIdle();		
	}

	public void testType() throws Exception {
		ParametedTypeFactory factory = new ParametedTypeFactory();
		IJavaProject jp = JavaCore.create(project);
		IType type = jp.findType("test.Test1");
		ParametedType t = (ParametedType)factory.newParametedType(type);
		R[] rs = new R[3];
		Thread[] ts = new Thread[rs.length];
		for (int i = 0; i < ts.length; i++) {
			rs[i] = new R(t);
			ts[i] = new Thread(rs[i]);
		}
		for (int i = 0; i < ts.length; i++) {
			ts[i].start();			
		}
		for (int i = 0; i < ts.length; i++) {
			ts[i].join();
		}
		for (int i = 0; i < ts.length; i++) {
			if(rs[i].exception != null) {
				fail("" + rs[i].exception);
			}
			assertEquals(11, rs[i].size);
		}		
	}

	class R implements Runnable {
		ParametedType t;
		int size;
		ConcurrentModificationException exception;

		public R(ParametedType t) {
			this.t = t;
		}

		@Override
		public void run() {
			Set<IParametedType> types = t.getAllTypes();
			size = types.size();
			try {
				for (IParametedType t1: types) {
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
					}
				}
			} catch (ConcurrentModificationException e) {
				exception = e;
			}
		}
	}

	public void tearDown() throws Exception {
		boolean saveAutoBuild = ResourcesUtils.setBuildAutomatically(false);
		JobUtils.waitForIdle();
		project.delete(true, true, null);
		JobUtils.waitForIdle();
		ResourcesUtils.setBuildAutomatically(saveAutoBuild);
	}

}