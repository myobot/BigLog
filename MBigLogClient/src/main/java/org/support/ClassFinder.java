package org.support;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author wangzhanwei
 */
public class ClassFinder {
    public ClassFinder() {
    }

    public static String packageToPath(String packageName) {
        return packageName.replaceAll("\\.", "/");
    }

    public static List<Class<?>> getAllClass() {
        return getAllClass("org.jow");
    }

    public static List<Class<?>> getAllClass(String pname) {
        Set<Class<?>> classes = new HashSet();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        String packageDirName = pname.replace('.', '/');

        try {
            Enumeration dirs = cl.getResources(packageDirName);

            while (dirs.hasMoreElements()) {
                URL url = (URL) dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    findByFile(cl, pname, URLDecoder.decode(url.getFile(), "utf-8"), classes);
                } else if ("jar".equals(protocol)) {
                    continue;
//                    findInJar(cl, pname, packageDirName, url, classes);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Class<?>> result = new ArrayList(classes);
        Collections.sort(result, Comparator.comparing(Class::getName));
        return result;
    }

    private static void findByFile(ClassLoader cl, String packageName, String filePath, Set<Class<?>> classes) {
        File dir = new File(filePath);
        if (dir.exists() && dir.isDirectory()) {
            File[] dirFiles = dir.listFiles(file -> file.isDirectory() || file.getName().endsWith(".class"));
            int length = dirFiles.length;

            for (int i = 0; i < length; ++i) {
                File file = dirFiles[i];
                if (file.isDirectory()) {
                    findByFile(cl, packageName + "." + file.getName(), file.getAbsolutePath(), classes);
                } else {
                    try {
                        String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                        Class<?> clazz = Class.forName(className);
                        classes.add(clazz);
                    } catch (ExceptionInInitializerError e) {
                        e.printStackTrace();
                    } catch (NoClassDefFoundError e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (Exception e) {

                    }
                }
            }

        }
    }

    private static void findInJar(ClassLoader cl, String pname, String packageDirName, URL url, Set<Class<?>> classes) {
        try {
            JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
            Enumeration entries = jar.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = (JarEntry) entries.nextElement();
                if (!entry.isDirectory()) {
                    String name = entry.getName();
                    if (name.charAt(0) == 47) {
                        name = name.substring(0);
                    }

                    if (name.startsWith(packageDirName) && name.contains("/") && name.endsWith(".class")) {
                        name = name.substring(0, name.length() - 6).replace('/', '.');

                        try {
                            Class<?> clazz = cl.loadClass(name);
                            classes.add(clazz);
                        } catch (Throwable e) {
                            System.out.println("无法直接加载的类：" + name);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
