////////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code for adherence to a set of rules.
// Copyright (C) 2001-2016 the original author or authors.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
////////////////////////////////////////////////////////////////////////////////

package com.puppycrawl.tools.checkstyle.checks.naming;

import static com.puppycrawl.tools.checkstyle.checks.naming.AbstractNameCheck.MSG_INVALID_PATTERN;
import static org.junit.Assert.assertArrayEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.BaseCheckTestSupport;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.api.Scope;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.CommonUtils;

public class ParameterNameCheckTest
    extends BaseCheckTestSupport {
    @Override
    protected String getPath(String filename) throws IOException {
        return super.getPath("checks" + File.separator
                + "naming" + File.separator + filename);
    }

    @Test
    public void testGetRequiredTokens() {
        final ParameterNameCheck checkObj = new ParameterNameCheck();
        final int[] expected = {TokenTypes.PARAMETER_DEF};
        assertArrayEquals(expected, checkObj.getRequiredTokens());
    }

    @Test
    public void testCatch()
            throws Exception {
        final DefaultConfiguration checkConfig =
            createCheckConfig(ParameterNameCheck.class);
        checkConfig.addAttribute("format", "^NO_WAY_MATEY$");
        final String[] expected = CommonUtils.EMPTY_STRING_ARRAY;
        verify(checkConfig, getPath("InputCatchOnly.java"), expected);
    }

    @Test
    public void testSpecified()
            throws Exception {
        final DefaultConfiguration checkConfig =
            createCheckConfig(ParameterNameCheck.class);
        checkConfig.addAttribute("format", "^a[A-Z][a-zA-Z0-9]*$");

        final String pattern = "^a[A-Z][a-zA-Z0-9]*$";

        final String[] expected = {
            "71:19: " + getCheckMessage(MSG_INVALID_PATTERN, "badFormat1", pattern),
            "71:34: " + getCheckMessage(MSG_INVALID_PATTERN, "badFormat2", pattern),
            "72:25: " + getCheckMessage(MSG_INVALID_PATTERN, "badFormat3", pattern),
        };
        verify(checkConfig, getPath("InputSimple.java"), expected);
    }

    @Test
    public void testDefault()
            throws Exception {
        final DefaultConfiguration checkConfig =
            createCheckConfig(ParameterNameCheck.class);
        final String[] expected = CommonUtils.EMPTY_STRING_ARRAY;
        verify(checkConfig, getPath("InputSimple.java"), expected);
    }

    @Test
    public void testGetAcceptableTokens() {
        final ParameterNameCheck parameterNameCheckObj = new ParameterNameCheck();
        final int[] actual = parameterNameCheckObj.getAcceptableTokens();
        final int[] expected = {
            TokenTypes.PARAMETER_DEF,
        };
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSkipMethodsWithOverrideAnnotationTrue()
            throws Exception {
        final DefaultConfiguration checkConfig =
            createCheckConfig(ParameterNameCheck.class);
        checkConfig.addAttribute("format", "^h$");
        checkConfig.addAttribute("ignoreOverridden", "true");

        final String pattern = "^h$";

        final String[] expected = {
            "11:28: " + getCheckMessage(MSG_INVALID_PATTERN, "object", pattern),
            "15:30: " + getCheckMessage(MSG_INVALID_PATTERN, "aaaa", pattern),
            "19:19: " + getCheckMessage(MSG_INVALID_PATTERN, "abc", pattern),
            "19:28: " + getCheckMessage(MSG_INVALID_PATTERN, "bd", pattern),
            "21:18: " + getCheckMessage(MSG_INVALID_PATTERN, "abc", pattern),
            "28:33: " + getCheckMessage(MSG_INVALID_PATTERN, "field", pattern),
            "28:62: " + getCheckMessage(MSG_INVALID_PATTERN, "packageNames", pattern),
            };
        verify(checkConfig, getPath("InputOverrideAnnotation.java"), expected);
    }

    @Test
    public void testSkipMethodsWithOverrideAnnotationFalse()
            throws Exception {
        final DefaultConfiguration checkConfig =
            createCheckConfig(ParameterNameCheck.class);
        checkConfig.addAttribute("format", "^h$");
        checkConfig.addAttribute("ignoreOverridden", "false");

        final String pattern = "^h$";

        final String[] expected = {
            "6:34: " + getCheckMessage(MSG_INVALID_PATTERN, "o", pattern),
            "11:28: " + getCheckMessage(MSG_INVALID_PATTERN, "object", pattern),
            "15:30: " + getCheckMessage(MSG_INVALID_PATTERN, "aaaa", pattern),
            "19:19: " + getCheckMessage(MSG_INVALID_PATTERN, "abc", pattern),
            "19:28: " + getCheckMessage(MSG_INVALID_PATTERN, "bd", pattern),
            "21:18: " + getCheckMessage(MSG_INVALID_PATTERN, "abc", pattern),
            "28:33: " + getCheckMessage(MSG_INVALID_PATTERN, "field", pattern),
            "28:62: " + getCheckMessage(MSG_INVALID_PATTERN, "packageNames", pattern),
            };
        verify(checkConfig, getPath("InputOverrideAnnotation.java"), expected);
    }

    @Test
    public void testScope()
            throws Exception {
        final DefaultConfiguration checkConfig =
            createCheckConfig(ParameterNameCheck.class);
        checkConfig.addAttribute("format", "^h$");
        checkConfig.addAttribute("scope", Scope.PUBLIC.getName());

        final String pattern = "^h$";

        final String[] expected = {
            "5:27: " + getCheckMessage(MSG_INVALID_PATTERN, "pubconstr", pattern),
            "9:31: " + getCheckMessage(MSG_INVALID_PATTERN, "inner", pattern),
            "19:24: " + getCheckMessage(MSG_INVALID_PATTERN, "pubpub", pattern),
            "30:21: " + getCheckMessage(MSG_INVALID_PATTERN, "pubifc", pattern),
            "44:24: " + getCheckMessage(MSG_INVALID_PATTERN, "packpub", pattern),
            "60:21: " + getCheckMessage(MSG_INVALID_PATTERN, "packifc", pattern),
            };
        verify(checkConfig, getPath("InputScope.java"), expected);
    }

    @Test
    public void testExcludeScope()
            throws Exception {
        final DefaultConfiguration checkConfig =
            createCheckConfig(ParameterNameCheck.class);
        checkConfig.addAttribute("format", "^h$");
        checkConfig.addAttribute("excludeScope", Scope.PROTECTED.getName());

        final String pattern = "^h$";

        final String[] expected = {
            "23:17: " + getCheckMessage(MSG_INVALID_PATTERN, "pubpack", pattern),
            "25:25: " + getCheckMessage(MSG_INVALID_PATTERN, "pubpriv", pattern),
            "48:17: " + getCheckMessage(MSG_INVALID_PATTERN, "packpack", pattern),
            "50:25: " + getCheckMessage(MSG_INVALID_PATTERN, "packpriv", pattern),
            "68:27: " + getCheckMessage(MSG_INVALID_PATTERN, "lexp", pattern),
            "70:23: " + getCheckMessage(MSG_INVALID_PATTERN, "limp", pattern),
            };
        verify(checkConfig, getPath("InputScope.java"), expected);
    }

    @Test
    public void testScopeExcludeScope()
            throws Exception {
        final DefaultConfiguration checkConfig =
            createCheckConfig(ParameterNameCheck.class);
        checkConfig.addAttribute("format", "^h$");
        checkConfig.addAttribute("scope", Scope.PACKAGE.getName());
        checkConfig.addAttribute("excludeScope", Scope.PUBLIC.getName());

        final String pattern = "^h$";

        final String[] expected = {
            "21:27: " + getCheckMessage(MSG_INVALID_PATTERN, "pubprot", pattern),
            "23:17: " + getCheckMessage(MSG_INVALID_PATTERN, "pubpack", pattern),
            "46:27: " + getCheckMessage(MSG_INVALID_PATTERN, "packprot", pattern),
            "48:17: " + getCheckMessage(MSG_INVALID_PATTERN, "packpack", pattern),
            };
        verify(checkConfig, getPath("InputScope.java"), expected);
    }

    @Test
    public void testIsOverriddenNoNullPointerException()
            throws Exception {
        final DefaultConfiguration checkConfig = createCheckConfig(ParameterNameCheck.class);
        checkConfig.addAttribute("format", "^[a-z][a-zA-Z0-9]*$");
        checkConfig.addAttribute("ignoreOverridden", "true");
        final String[] expected = CommonUtils.EMPTY_STRING_ARRAY;
        verify(checkConfig, getPath("InputOverrideAnnotationNoNPE.java"), expected);
    }

    @Test
    public void testReceiverParameter() throws Exception {
        final DefaultConfiguration checkConfig = createCheckConfig(ParameterNameCheck.class);
        final String[] expected = CommonUtils.EMPTY_STRING_ARRAY;
        verify(checkConfig, getPath("InputParameterNameReceiver.java"), expected);
    }
}
