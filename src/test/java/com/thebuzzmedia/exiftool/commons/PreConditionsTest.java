/**
 * Copyright 2015 Mickael Jeanroy <mickael.jeanroy@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thebuzzmedia.exiftool.commons;

import com.thebuzzmedia.exiftool.exceptions.UnreadableFileException;
import com.thebuzzmedia.exiftool.exceptions.UnwritableFileException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PreConditionsTest {

	@Rule
	public ExpectedException thrown = none();

	@Test
	public void it_should_fail_with_npe() {
		String message = "should not be null";
		thrown.expect(NullPointerException.class);
		thrown.expectMessage(message);

		PreConditions.notNull(null, message);
	}

	@Test
	public void it_should_not_fail_with_npe() {
		String val = "foo";
		String foo = PreConditions.notNull(val, "should not be null");
		assertThat(foo)
			.isNotNull()
			.isEqualTo(val);
	}

	@Test
	public void it_should_fail_with_null_string() {
		String message = "should not be empty";
		thrown.expect(NullPointerException.class);
		thrown.expectMessage(message);

		PreConditions.notBlank(null, message);
	}

	@Test
	public void it_should_fail_with_empty_string() {
		String message = "should not be empty";
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(message);

		PreConditions.notBlank("", message);
	}

	@Test
	public void it_should_fail_with_blank_string() {
		String message = "should not be empty";
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(message);

		PreConditions.notBlank("  ", message);
	}

	@Test
	public void it_should_not_fail_with_valid_string() {
		String message = "should not be empty";
		String val = "foo";

		String result = PreConditions.notBlank(val, message);

		assertThat(result)
			.isNotNull()
			.isNotEmpty()
			.isEqualTo(val);
	}

	@Test
	public void it_should_fail_with_null_array() {
		String message = "should not be empty";
		thrown.expect(NullPointerException.class);
		thrown.expectMessage(message);

		PreConditions.notEmpty((Object[]) null, message);
	}

	@Test
	public void it_should_fail_with_empty_array() {
		String message = "should not be empty";
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(message);

		PreConditions.notEmpty(new String[]{ }, message);
	}

	@Test
	public void it_should_not_fail_with_valid_array() {
		String message = "should not be empty";
		String[] val = new String[] { "foo" };

		String[] results = PreConditions.notEmpty(val, message);

		assertThat(results)
			.isNotNull()
			.isNotEmpty()
			.isSameAs(val);
	}

	@Test
	public void it_should_fail_with_null_map() {
		String message = "should not be empty";
		thrown.expect(NullPointerException.class);
		thrown.expectMessage(message);

		PreConditions.notEmpty((Map<Object, Object>) null, message);
	}

	@Test
	public void it_should_fail_with_empty_map() {
		String message = "should not be empty";
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(message);

		PreConditions.notEmpty(emptyMap(), message);
	}

	@Test
	public void it_should_not_fail_with_valid_map() {
		String message = "should not be empty";
		Map<String, String> val = new HashMap<String, String>();
		val.put("foo", "bar");

		Map<String, String> results = PreConditions.notEmpty(val, message);

		assertThat(results)
			.isNotNull()
			.isNotEmpty()
			.isSameAs(val);
	}

	@Test
	public void it_should_not_be_readable_with_null_file() {
		String message = "should be readable";
		thrown.expect(NullPointerException.class);
		thrown.expectMessage(message);

		PreConditions.isReadable(null, message);
	}

	@Test
	public void it_should_not_be_readable_with_file_that_does_not_exist() {
		String message = "should be readable";
		thrown.expect(UnreadableFileException.class);
		thrown.expectMessage(message);

		File file = mock(File.class);
		when(file.exists()).thenReturn(false);

		PreConditions.isReadable(file, message);
	}

	@Test
	public void it_should_fail_with_file_that_is_not_readable() {
		String message = "should be readable";
		thrown.expect(UnreadableFileException.class);
		thrown.expectMessage(message);

		File file = mock(File.class);
		when(file.exists()).thenReturn(true);
		when(file.canRead()).thenReturn(false);

		PreConditions.isReadable(file, message);
	}

	@Test
	public void it_should_not_fail_with_readable_file() {
		String message = "should be readable";

		File file = mock(File.class);
		when(file.exists()).thenReturn(true);
		when(file.canRead()).thenReturn(true);

		File result = PreConditions.isReadable(file, message);

		assertThat(result)
			.isNotNull()
			.isSameAs(file);
	}

	@Test
	public void it_should_not_be_writable_with_null_file() {
		String message = "should be writable";
		thrown.expect(NullPointerException.class);
		thrown.expectMessage(message);

		PreConditions.isWritable(null, message);
	}

	@Test
	public void it_should_not_be_writable_with_file_that_does_not_exist() {
		String message = "should be writable";
		thrown.expect(UnwritableFileException.class);
		thrown.expectMessage(message);

		File file = mock(File.class);
		when(file.exists()).thenReturn(false);

		PreConditions.isWritable(file, message);
	}

	@Test
	public void it_should_fail_with_file_that_is_not_writable() {
		String message = "should be writable";
		thrown.expect(UnwritableFileException.class);
		thrown.expectMessage(message);

		File file = mock(File.class);
		when(file.exists()).thenReturn(true);
		when(file.canWrite()).thenReturn(false);

		PreConditions.isWritable(file, message);
	}

	@Test
	public void it_should_not_fail_with_writable_file() {
		String message = "should be writable";

		File file = mock(File.class);
		when(file.exists()).thenReturn(true);
		when(file.canWrite()).thenReturn(true);

		File result = PreConditions.isWritable(file, message);

		assertThat(result)
			.isNotNull()
			.isSameAs(file);
	}
}