/*
 * Copyright (C) 2023 The Dora Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dora.util;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Linux command line related tools.
 * 简体中文：Linux命令行相关工具。
 */
public final class ShellUtils {

    private static final String LINE_SEP = System.getProperty("line.separator");

    private static final String[] suPaths = {
            "/data/local/",
            "/data/local/bin/",
            "/data/local/xbin/",
            "/sbin/",
            "/su/bin/",
            "/system/bin/",
            "/system/bin/.ext/",
            "/system/bin/failsafe/",
            "/system/sd/xbin/",
            "/system/usr/we-need-root/",
            "/system/xbin/",
            "/cache/",
            "/data/",
            "/dev/"
    };

    private ShellUtils() {
    }

    /**
     * A variation on the checking for SU, this attempts a 'which su'.
     * 简体中文：这是一种检查 SU（超级用户）权限的变体方式，它尝试执行一次 which su 命令。
     *
     * @return true if su found 简体中文：如果找到 su，则返回 true。
     */
    public boolean checkSuExists() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[] { "which", "su" });
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return in.readLine() != null;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }

    /**
     * Get a list of paths to check for binaries.
     * 简体中文：获取要检查二进制文件的路径列表。
     *
     * @return List of paths to check, using a combination of a static list and those paths
     * listed in the PATH environment variable. 简体中文：要检查的路径列表，结合了静态路径列表和 PATH 环境变
     * 量中列出的路径。
     */
    static String[] getBinariesPaths() {
        ArrayList<String> paths = new ArrayList<>(Arrays.asList(suPaths));
        String sysPaths = System.getenv("PATH");
        if (sysPaths == null || "".equals(sysPaths)) {
            return paths.toArray(new String[0]);
        }
        for (String path : sysPaths.split(":")) {
            if (!path.endsWith("/")) {
                path = path + '/';
            }
            if (!paths.contains(path)) {
                paths.add(path);
            }
        }
        return paths.toArray(new String[0]);
    }

    /**
     * Execute the command asynchronously.
     * 简体中文：异步执行该命令。
     *
     * @param command  The command.
     * @param isRooted True to use root, false otherwise.
     * @param consumer The consumer.
     * @return the task
     */
    public static ThreadUtils.DoraTask<CommandResult> execCmdAsync(final String command,
                                                                   final boolean isRooted,
                                                                   final ThreadUtils.Consumer<CommandResult> consumer) {
        return execCmdAsync(new String[]{command}, isRooted, true, consumer);
    }

    /**
     * Execute the command asynchronously.
     * 简体中文：异步执行该命令。
     *
     * @param commands The commands.
     * @param isRooted True to use root, false otherwise.
     * @param consumer The consumer.
     * @return the task
     */
    public static ThreadUtils.DoraTask<CommandResult> execCmdAsync(final List<String> commands,
                                                                   final boolean isRooted,
                                                                   final ThreadUtils.Consumer<CommandResult> consumer) {
        return execCmdAsync(commands == null ? null : commands.toArray(new String[]{}), isRooted, true, consumer);
    }

    /**
     * Execute the command asynchronously.
     * 简体中文：异步执行该命令。
     *
     * @param commands The commands.
     * @param isRooted True to use root, false otherwise.
     * @param consumer The consumer.
     * @return the task
     */
    public static ThreadUtils.DoraTask<CommandResult> execCmdAsync(final String[] commands,
                                                                   final boolean isRooted,
                                                                   final ThreadUtils.Consumer<CommandResult> consumer) {
        return execCmdAsync(commands, isRooted, true, consumer);
    }

    /**
     * Execute the command asynchronously.
     * 简体中文：异步执行该命令。
     *
     * @param command         The command.
     * @param isRooted        True to use root, false otherwise.
     * @param isNeedResultMsg True to return the message of result, false otherwise.
     * @param consumer        The consumer.
     * @return the task
     */
    public static ThreadUtils.DoraTask<CommandResult> execCmdAsync(final String command,
                                                                   final boolean isRooted,
                                                                   final boolean isNeedResultMsg,
                                                                   final ThreadUtils.Consumer<CommandResult> consumer) {
        return execCmdAsync(new String[]{command}, isRooted, isNeedResultMsg, consumer);
    }

    /**
     * Execute the command asynchronously.
     * 简体中文：异步执行该命令。
     *
     * @param commands        The commands.
     * @param isRooted        True to use root, false otherwise.
     * @param isNeedResultMsg True to return the message of result, false otherwise.
     * @param consumer        The consumer.
     * @return the task
     */
    public static ThreadUtils.DoraTask<CommandResult> execCmdAsync(final List<String> commands,
                                                                   final boolean isRooted,
                                                                   final boolean isNeedResultMsg,
                                                                   final ThreadUtils.Consumer<CommandResult> consumer) {
        return execCmdAsync(commands == null ? null : commands.toArray(new String[]{}),
                isRooted,
                isNeedResultMsg,
                consumer);
    }

    /**
     * Execute the command asynchronously.
     * 简体中文：异步执行该命令。
     *
     * @param commands        The commands.
     * @param isRooted        True to use root, false otherwise.
     * @param isNeedResultMsg True to return the message of result, false otherwise.
     * @param consumer        The consumer.
     * @return the task
     */
    public static ThreadUtils.DoraTask<CommandResult> execCmdAsync(final String[] commands,
                                                                   final boolean isRooted,
                                                                   final boolean isNeedResultMsg,
                                                                   @NonNull final ThreadUtils.Consumer<CommandResult> consumer) {
        return doAsync(new ThreadUtils.DoraTask<CommandResult>(consumer) {
            @Override
            public CommandResult doInBackground() {
                return execCmd(commands, isRooted, isNeedResultMsg);
            }
        });
    }

    static <T> ThreadUtils.DoraTask<T> doAsync(final ThreadUtils.DoraTask<T> task) {
        ThreadUtils.getCachedPool().execute(task);
        return task;
    }

    /**
     * Execute the command.
     * 简体中文：执行该命令。
     *
     * @param command  The command.
     * @param isRooted True to use root, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final String command, final boolean isRooted) {
        return execCmd(new String[]{command}, isRooted, true);
    }

    /**
     * Execute the command.
     * 简体中文：执行该命令。
     *
     * @param command  The command.
     * @param envp     The environment variable settings.
     * @param isRooted True to use root, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final String command, final List<String> envp, final boolean isRooted) {
        return execCmd(new String[]{command},
                envp == null ? null : envp.toArray(new String[]{}),
                isRooted,
                true);
    }

    /**
     * Execute the command.
     * 简体中文：执行该命令。
     *
     * @param commands The commands.
     * @param isRooted True to use root, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final List<String> commands, final boolean isRooted) {
        return execCmd(commands == null ? null : commands.toArray(new String[]{}), isRooted, true);
    }

    /**
     * Execute the command.
     * 简体中文：执行该命令。
     *
     * @param commands The commands.
     * @param envp     The environment variable settings.
     * @param isRooted True to use root, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final List<String> commands,
                                        final List<String> envp,
                                        final boolean isRooted) {
        return execCmd(commands == null ? null : commands.toArray(new String[]{}),
                envp == null ? null : envp.toArray(new String[]{}),
                isRooted,
                true);
    }

    /**
     * Execute the command.
     * 简体中文：执行该命令。
     *
     * @param commands The commands.
     * @param isRooted True to use root, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final String[] commands, final boolean isRooted) {
        return execCmd(commands, isRooted, true);
    }

    /**
     * Execute the command.
     * 简体中文：执行该命令。
     *
     * @param command         The command.
     * @param isRooted        True to use root, false otherwise.
     * @param isNeedResultMsg True to return the message of result, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final String command,
                                        final boolean isRooted,
                                        final boolean isNeedResultMsg) {
        return execCmd(new String[]{command}, isRooted, isNeedResultMsg);
    }

    /**
     * Execute the command.
     * 简体中文：执行该命令。
     *
     * @param command         The command.
     * @param envp            The environment variable settings.
     * @param isRooted        True to use root, false otherwise.
     * @param isNeedResultMsg True to return the message of result, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final String command,
                                        final List<String> envp,
                                        final boolean isRooted,
                                        final boolean isNeedResultMsg) {
        return execCmd(new String[]{command}, envp == null ? null : envp.toArray(new String[]{}),
                isRooted,
                isNeedResultMsg);
    }

    /**
     * Execute the command.
     * 简体中文：执行该命令。
     *
     * @param command         The command.
     * @param envp            The environment variable settings array.
     * @param isRooted        True to use root, false otherwise.
     * @param isNeedResultMsg True to return the message of result, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final String command,
                                        final String[] envp,
                                        final boolean isRooted,
                                        final boolean isNeedResultMsg) {
        return execCmd(new String[]{command}, envp, isRooted, isNeedResultMsg);
    }

    /**
     * Execute the command.
     * 简体中文：执行该命令。
     *
     * @param commands        The commands.
     * @param isRooted        True to use root, false otherwise.
     * @param isNeedResultMsg True to return the message of result, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final List<String> commands,
                                        final boolean isRooted,
                                        final boolean isNeedResultMsg) {
        return execCmd(commands == null ? null : commands.toArray(new String[]{}),
                isRooted,
                isNeedResultMsg);
    }

    /**
     * Execute the command.
     * 简体中文：执行该命令。
     *
     * @param commands        The commands.
     * @param isRooted        True to use root, false otherwise.
     * @param isNeedResultMsg True to return the message of result, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final String[] commands,
                                        final boolean isRooted,
                                        final boolean isNeedResultMsg) {
        return execCmd(commands, null, isRooted, isNeedResultMsg);
    }

    /**
     * Execute the command.
     * 简体中文：执行该命令。
     *
     * @param commands        The commands.
     * @param envp            Array of strings, each element of which
     *                        has environment variable settings in the format
     *                        <i>name</i>=<i>value</i>, or
     *                        <tt>null</tt> if the subprocess should inherit
     *                        the environment of the current process.
     * @param isRooted        True to use root, false otherwise.
     * @param isNeedResultMsg True to return the message of result, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final String[] commands,
                                        final String[] envp,
                                        final boolean isRooted,
                                        final boolean isNeedResultMsg) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new CommandResult(result, "", "");
        }
        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec(isRooted ? "su" : "sh", envp, null);
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) continue;
                os.write(command.getBytes());
                os.writeBytes(LINE_SEP);
                os.flush();
            }
            os.writeBytes("exit" + LINE_SEP);
            os.flush();
            result = process.waitFor();
            if (isNeedResultMsg) {
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = new BufferedReader(
                        new InputStreamReader(process.getInputStream(), "UTF-8")
                );
                errorResult = new BufferedReader(
                        new InputStreamReader(process.getErrorStream(), "UTF-8")
                );
                String line;
                if ((line = successResult.readLine()) != null) {
                    successMsg.append(line);
                    while ((line = successResult.readLine()) != null) {
                        successMsg.append(LINE_SEP).append(line);
                    }
                }
                if ((line = errorResult.readLine()) != null) {
                    errorMsg.append(line);
                    while ((line = errorResult.readLine()) != null) {
                        errorMsg.append(LINE_SEP).append(line);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (successResult != null) {
                    successResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }
        return new CommandResult(
                result,
                successMsg == null ? "" : successMsg.toString(),
                errorMsg == null ? "" : errorMsg.toString()
        );
    }

    /**
     * The result of command.
     * 简体中文：命令的结果。
     */
    public static class CommandResult {
        public int    result;
        public String successMsg;
        public String errorMsg;

        public CommandResult(final int result, final String successMsg, final String errorMsg) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }

        @Override
        public String toString() {
            return "result: " + result + "\n" +
                    "successMsg: " + successMsg + "\n" +
                    "errorMsg: " + errorMsg;
        }
    }
}
