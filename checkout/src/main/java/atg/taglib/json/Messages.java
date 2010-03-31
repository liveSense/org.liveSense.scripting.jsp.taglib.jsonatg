/**
 * Copyright 2007 Art Technology Group, Inc (ATG)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package atg.taglib.json;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * i18n Messages Implementation (provided by Eclipse)
 *
 * @author James Wiltshire
 * @version $Id$
 */

public class Messages
{
  private static final String BUNDLE_NAME = "atg.taglib.json.messages"; //$NON-NLS-1$

  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

  private Messages()
  {
    // Can't be instantiated
  }

  public static String getString(String key)
  {
    try {
      return RESOURCE_BUNDLE.getString(key);
    }
    catch (MissingResourceException e) {
      return '!' + key + '!';
    }
  }
}
