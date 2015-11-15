/**
 * Copyright 2011 The Buzz Media, LLC
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

package com.thebuzzmedia.exiftool;

import com.thebuzzmedia.exiftool.exceptions.UnreadableFileException;
import com.thebuzzmedia.exiftool.process.Command;
import com.thebuzzmedia.exiftool.process.Executor;
import com.thebuzzmedia.exiftool.process.Executors;
import com.thebuzzmedia.exiftool.process.Result;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Executors.class)
public class ExifTool_getImageMeta_Test {

	@Rule
	public ExpectedException thrown = none();

	private ExifTool exifTool;

	private Executor executor;

	@Before
	public void setUp() {
		executor = mock(Executor.class);

		PowerMockito.mockStatic(Executors.class);
		PowerMockito.when(Executors.newExecutor()).thenReturn(executor);

		Result resultVersion = mock(Result.class);
		when(resultVersion.getOutput()).thenReturn("9.36");
		when(resultVersion.getExitStatus()).thenReturn(0);
		when(resultVersion.isSuccess()).thenReturn(true);
		when(executor.execute(any(Command.class))).thenReturn(resultVersion);

		exifTool = new ExifTool();
		assertThat(exifTool.getVersion()).isEqualTo("9.36");

		reset(executor);
	}

	@Test
	public void it_should_fail_if_image_is_null() throws Exception {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Image cannot be null and must be a valid stream of image data.");

		exifTool.getImageMeta(null, Format.HUMAN_READABLE, Tag.values());
	}

	@Test
	public void it_should_fail_if_format_is_null() throws Exception {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Format cannot be null.");

		exifTool.getImageMeta(mock(File.class), null, Tag.values());
	}

	@Test
	public void it_should_fail_if_tags_is_null() throws Exception {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Tags cannot be null and must contain 1 or more Tag to query the image for.");

		exifTool.getImageMeta(mock(File.class), Format.HUMAN_READABLE, null);
	}

	@Test
	public void it_should_fail_if_tags_is_empty() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Tags cannot be null and must contain 1 or more Tag to query the image for.");

		exifTool.getImageMeta(mock(File.class), Format.HUMAN_READABLE, new Tag[]{ });
	}

	@Test
	public void it_should_fail_with_unknown_file() throws Exception {
		thrown.expect(UnreadableFileException.class);
		thrown.expectMessage("Unable to read the given image [/foo.png], ensure that the image exists at the given path and that the executing Java process has permissions to read it.");

		File image = mock(File.class);
		when(image.getPath()).thenReturn("/foo.png");
		when(image.exists()).thenReturn(false);
		when(image.canRead()).thenReturn(true);
		when(image.toString()).thenCallRealMethod();

		exifTool.getImageMeta(image, Format.HUMAN_READABLE, Tag.values());
	}

	@Test
	public void it_should_fail_with_non_readable_file() throws Exception {
		thrown.expect(UnreadableFileException.class);
		thrown.expectMessage("Unable to read the given image [/foo.png], ensure that the image exists at the given path and that the executing Java process has permissions to read it.");

		File image = mock(File.class);
		when(image.getPath()).thenReturn("/foo.png");
		when(image.exists()).thenReturn(true);
		when(image.canRead()).thenReturn(false);
		when(image.toString()).thenCallRealMethod();

		exifTool.getImageMeta(image, Format.HUMAN_READABLE, Tag.values());
	}
}