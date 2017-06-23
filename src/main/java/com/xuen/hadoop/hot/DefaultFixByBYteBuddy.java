package com.xuen.hadoop.hot;

import static net.bytebuddy.dynamic.ClassFileLocator.CLASS_FILE_EXTENSION;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.xuen.hadoop.hot.DefaultFixByBYteBuddy.AgentProvider.ForByteBuddyAgent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.ByteBuddyAgent.AttachmentProvider;
import net.bytebuddy.agent.ByteBuddyAgent.ProcessProvider;
import net.bytebuddy.agent.Installer;

/**
 * @author zheng.xu
 * @since 2017-06-23
 */
public class DefaultFixByBYteBuddy implements FixByByteBuddy {


    @Override
    public Instrumentation getInstrumentationByPid(String pid) {
        return ByteBuddyAgent
                .install(AttachmentProvider.DEFAULT, new ProcessProvider() {
                    @Override
                    public String resolve() {
                        return pid;
                    }
                });
    }

    @Override
    public void attach(String pid, String jarfilePath) {
        ByteBuddyAgent.attach(new File(jarfilePath), pid);
    }

    @Override
    public Instrumentation getInstrumentation() {
        return ByteBuddyAgent.install();
    }


    public static void main(String[] args)
            throws InterruptedException, UnmodifiableClassException, ClassNotFoundException, IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException, InvocationTargetException, IllegalAccessException {

//        File file = null;
//        Class<ByteBuddyAgent> forByteBuddyAgentClass = ByteBuddyAgent.class;
//        Method[] declaredMethods = forByteBuddyAgentClass.getDeclaredMethods();
//        for (Method method: declaredMethods){
//            if (method.getName().equals("resolve")){
//                Object invoke = method.invoke(forByteBuddyAgentClass);
//                file = (File) invoke;
//
//            }
//        }
        File file = ForByteBuddyAgent.INSTANCE.resolve();
        VirtualMachine virtualMachine = VirtualMachine.attach("22633");
        String id = virtualMachine.id();

        virtualMachine.loadAgent(file.getAbsolutePath());
        System.out.println(id);

    }



    protected interface AgentProvider {

        /**
         * Provides an agent jar file for attachment.
         *
         * @return The provided agent.
         * @throws IOException If the agent cannot be written to disk.
         */
        File resolve() throws IOException;

        /**
         * An agent provider for a temporary Byte Buddy agent.
         */
        enum ForByteBuddyAgent implements AgentProvider {

            /**
             * The singleton instance.
             */
            INSTANCE;

            /**
             * The default prefix of the Byte Buddy agent jar file.
             */
            private static final String AGENT_FILE_NAME = "byteBuddyAgent";

            /**
             * The jar file extension.
             */
            private static final String JAR_FILE_EXTENSION = ".jar";
            private int BUFFER_SIZE = 1024;
            private static final int START_INDEX = 0, END_OF_FILE = -1;

            private static final String AGENT_CLASS_PROPERTY = "Agent-Class";

            /**
             * The manifest property specifying the <i>can redefine</i> property.
             */
            private static final String CAN_REDEFINE_CLASSES_PROPERTY = "Can-Redefine-Classes";

            /**
             * The manifest property specifying the <i>can retransform</i> property.
             */
            private static final String CAN_RETRANSFORM_CLASSES_PROPERTY = "Can-Retransform-Classes";

            /**
             * The manifest property specifying the <i>can set native method prefix</i> property.
             */
            private static final String CAN_SET_NATIVE_METHOD_PREFIX = "Can-Set-Native-Method-Prefix";
            private static final String MANIFEST_VERSION_VALUE = "1.0";
            @Override
            public File resolve() throws IOException {
                File agentJar;
                InputStream inputStream = Installer.class.getResourceAsStream('/' + Installer.class.getName().replace('.', '/') + CLASS_FILE_EXTENSION);
                if (inputStream == null) {
                    throw new IllegalStateException("Cannot locate class file for Byte Buddy installer");
                }
                try {
                    agentJar = File.createTempFile(AGENT_FILE_NAME, JAR_FILE_EXTENSION);
                    agentJar.deleteOnExit(); // Agent jar is required until VM shutdown due to lazy class loading.
                    Manifest manifest = new Manifest();
                    manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, MANIFEST_VERSION_VALUE);
                    manifest.getMainAttributes().put(new Attributes.Name(AGENT_CLASS_PROPERTY), Installer.class.getName());
                    manifest.getMainAttributes().put(new Attributes.Name(CAN_REDEFINE_CLASSES_PROPERTY), Boolean.TRUE.toString());
                    manifest.getMainAttributes().put(new Attributes.Name(CAN_RETRANSFORM_CLASSES_PROPERTY), Boolean.TRUE.toString());
                    manifest.getMainAttributes().put(new Attributes.Name(CAN_SET_NATIVE_METHOD_PREFIX), Boolean.TRUE.toString());
                    JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(agentJar), manifest);
                    try {
                        jarOutputStream.putNextEntry(new JarEntry(Installer.class.getName().replace('.', '/') + CLASS_FILE_EXTENSION));
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int index;
                        while ((index = inputStream.read(buffer)) != END_OF_FILE) {
                            jarOutputStream.write(buffer, START_INDEX, index);
                        }
                        jarOutputStream.closeEntry();
                    } finally {
                        jarOutputStream.close();
                    }
                } finally {
                    inputStream.close();
                }
                return agentJar;
            }
        }


    }
}
