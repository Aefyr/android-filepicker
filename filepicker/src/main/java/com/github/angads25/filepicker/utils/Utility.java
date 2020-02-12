/*
 * Copyright (C) 2016 Angad Singh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.angads25.filepicker.utils;

import android.content.Context;
import android.content.pm.PackageManager;

import com.github.angads25.filepicker.R;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.model.FileListItem;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

/**
 * <p>
 * Created by Angad Singh on 11-07-2016.
 * </p>
 */
public class Utility {
    /**
     * Post Lollipop Devices require permissions on Runtime (Risky Ones), even though it has been
     * specified in the uses-permission tag of manifest. checkStorageAccessPermissions
     * method checks whether the READ EXTERNAL STORAGE permission has been granted to
     * the Application.
     *
     * @return a boolean value notifying whether the permission is granted or not.
     */
    public static boolean checkStorageAccessPermissions(Context context) {   //Only for Android M and above.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            String permission = "android.permission.READ_EXTERNAL_STORAGE";
            int res = context.checkCallingOrSelfPermission(permission);
            return (res == PackageManager.PERMISSION_GRANTED);
        } else {   //Pre Marshmallow can rely on Manifest defined permissions.
            return true;
        }
    }

    /**
     * Prepares the list of Files and Folders inside 'inter' Directory.
     * The list can be filtered through extensions. 'filter' reference
     * is the FileFilter. A reference of ArrayList is passed, in case it
     * may contain the ListItem for parent directory. Returns the List of
     * Directories/files in the form of ArrayList.
     *
     * @param internalList ArrayList containing parent directory.
     * @param inter        The present directory to look into.
     * @param filter       Extension filter class reference, for filtering files.
     * @return ArrayList of FileListItem containing file info of current directory.
     */
    public static ArrayList<FileListItem> prepareFileListEntries(ArrayList<FileListItem> internalList, File inter, ExtensionFilter filter, Comparator<FileListItem> sorter) {
        try {
            //Check for each and every directory/file in 'inter' directory.
            //Filter by extension using 'filter' reference.

            for (File name : inter.listFiles(filter)) {
                //If file/directory can be read by the Application
                if (name.canRead()) {
                    //Create a row item for the directory list and define properties.
                    FileListItem item = new FileListItem();
                    item.setFilename(name.getName());
                    item.setDirectory(name.isDirectory());
                    item.setLocation(name.getAbsolutePath());
                    item.setTime(name.lastModified());
                    item.setSize(name.length());
                    //Add row to the List of directories/files
                    internalList.add(item);
                }
            }
            //Sort the files and directories in alphabetical order.
            //See compareTo method in FileListItem class.
            Collections.sort(internalList, sorter);
        } catch (NullPointerException e) {   //Just dont worry, it rarely occurs.
            e.printStackTrace();
            internalList = new ArrayList<>();
        }
        return internalList;
    }

    public static Comparator<FileListItem> createFileListItemsComparator(DialogProperties properties) {
        final Comparator<FileListItem> comparator;
        final boolean reversed = properties.sortOrder == DialogConfigs.SORT_ORDER_REVERSE;

        switch (properties.sortBy) {
            case DialogConfigs.SORT_BY_LAST_MODIFIED:
                comparator = new Comparator<FileListItem>() {
                    @Override
                    public int compare(FileListItem item1, FileListItem item2) {
                        if (item2.isDirectory() && item1.isDirectory()) {
                            if (item1.getFilename().equals("..."))
                                return -1;

                            if (item2.getFilename().equals("..."))
                                return 1;

                            return -Long.compare(item1.getTime(), item2.getTime()) * (reversed ? -1 : 1);
                        } else if (!item2.isDirectory() && !item1.isDirectory()) {   //If the comparison is not between two directories, return the file with
                            //alphabetic order first.
                            return -Long.compare(item1.getTime(), item2.getTime()) * (reversed ? -1 : 1);
                        } else if (item2.isDirectory() && !item1.isDirectory()) {   //If the comparison is between a directory and a file, return the directory.
                            return 1;
                        } else {   //Same as above but order of occurence is different.
                            return -1;
                        }
                    }
                };
                break;
            case DialogConfigs.SORT_BY_NAME:
                comparator = new Comparator<FileListItem>() {
                    @Override
                    public int compare(FileListItem item1, FileListItem item2) {
                        if (item2.isDirectory() && item1.isDirectory()) {
                            if (item1.getFilename().equals("..."))
                                return -1;

                            if (item2.getFilename().equals("..."))
                                return 1;

                            return item1.getFilename().toLowerCase().compareTo(item2.getFilename().toLowerCase(Locale.getDefault())) * (reversed ? -1 : 1);
                        } else if (!item2.isDirectory() && !item1.isDirectory()) {   //If the comparison is not between two directories, return the file with
                            //alphabetic order first.
                            return item1.getFilename().toLowerCase().compareTo(item2.getFilename().toLowerCase(Locale.getDefault())) * (reversed ? -1 : 1);
                        } else if (item2.isDirectory() && !item1.isDirectory()) {   //If the comparison is between a directory and a file, return the directory.
                            return 1;
                        } else {   //Same as above but order of occurence is different.
                            return -1;
                        }
                    }
                };
                break;
            case DialogConfigs.SORT_BY_SIZE:
                comparator = new Comparator<FileListItem>() {
                    @Override
                    public int compare(FileListItem item1, FileListItem item2) {
                        if (item2.isDirectory() && item1.isDirectory()) {
                            if (item1.getFilename().equals("..."))
                                return -1;

                            if (item2.getFilename().equals("..."))
                                return 1;

                            return item1.getFilename().toLowerCase().compareTo(item2.getFilename().toLowerCase(Locale.getDefault()));
                        } else if (!item2.isDirectory() && !item1.isDirectory()) {   //If the comparison is not between two directories, return the file with
                            //alphabetic order first.
                            return -Long.compare(item1.getSize(), item2.getSize()) * (reversed ? -1 : 1);
                        } else if (item2.isDirectory() && !item1.isDirectory()) {   //If the comparison is between a directory and a file, return the directory.
                            return 1;
                        } else {   //Same as above but order of occurence is different.
                            return -1;
                        }
                    }
                };
                break;
            default:
                comparator = new Comparator<FileListItem>() {
                    @Override
                    public int compare(FileListItem o1, FileListItem o2) {
                        return o1.compareTo(o2);
                    }
                };
        }

        return comparator;
    }

    private static DecimalFormat sSizeDecimalFormat;

    public static String formatSize(Context c, long bytes) {
        if (sSizeDecimalFormat == null) {
            sSizeDecimalFormat = new DecimalFormat("#.##");
            sSizeDecimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        }

        String[] units = c.getResources().getStringArray(R.array.size_units);

        for (int i = 0; i < units.length; i++) {

            float size = (float) bytes / (float) Math.pow(1024, i);

            if (size < 1024)
                return String.format("%s %s", sSizeDecimalFormat.format(size), units[i]);

        }

        return bytes + " B";
    }
}
