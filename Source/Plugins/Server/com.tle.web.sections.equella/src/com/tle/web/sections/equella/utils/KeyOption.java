/*
 * Copyright 2017 Apereo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tle.web.sections.equella.utils;

import com.tle.common.i18n.CurrentLocale;
import com.tle.web.sections.standard.model.SimpleOption;

public class KeyOption<T> extends SimpleOption<T>
{

	public KeyOption(String name, String value, T obj)
	{
		super(name, value, obj);
	}

	@Override
	public String getName()
	{
		return CurrentLocale.get(super.getName());
	}

}