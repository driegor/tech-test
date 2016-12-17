package com.company.mockito;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

public class MockitoTest {

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
}