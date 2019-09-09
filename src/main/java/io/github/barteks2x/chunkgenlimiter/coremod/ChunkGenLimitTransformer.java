/*
 *  This file is part of ChunkGenLimiter, licensed under the MIT License (MIT).
 *
 *  Copyright (c) 2019 Barteks2x
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package io.github.barteks2x.chunkgenlimiter.coremod;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ChunkGenLimitTransformer implements IClassTransformer {

    private static final Logger LOGGER = LogManager.getLogger("ChunkGenLimitTransformer");

    @Override public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (!"net.minecraft.server.management.PlayerChunkMap".equals(transformedName)) {
            return basicClass;
        }
        LOGGER.info("Transforming net.minecraft.server.management.PlayerChunkMap");
        ClassReader reader = new ClassReader(basicClass);
        ClassWriter writer = new ClassWriter(Opcodes.ASM5);

        String targetMethod = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(
                "net/minecraft/server/management/PlayerChunkMap", "func_72693_b", "()V");

        LOGGER.info(" > Finding method " + targetMethod);

        ClassVisitor visitor = new ClassVisitor(Opcodes.ASM5, writer) {
            @Override public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                if (name.equals(targetMethod)) {
                    LOGGER.info(" > Transforming PlayerChunkMap.tick()");

                    return new MethodVisitor(Opcodes.ASM5, super.visitMethod(access, name, desc, signature, exceptions)) {
                        @Override public void visitIntInsn(int opcode, int operand) {
                            if (opcode == Opcodes.BIPUSH && operand == 49) {
                                LOGGER.info(" >>> Replacing constant 49 with getChunkLimit");
                                super.visitMethodInsn(Opcodes.INVOKESTATIC,
                                        "io/github/barteks2x/chunkgenlimiter/coremod/Hooks",
                                        "getChunkLimit",
                                        "()I", false);
                            } else {
                                super.visitIntInsn(opcode, operand);
                            }
                        }

                        @Override public void visitLdcInsn(Object cst) {
                            if (cst instanceof Long && (Long) cst == 50000000L) {
                                LOGGER.info(" >>> Replacing constant 50000000L with getTimeLimit");
                                super.visitMethodInsn(Opcodes.INVOKESTATIC,
                                        "io/github/barteks2x/chunkgenlimiter/coremod/Hooks",
                                        "getTimeLimit",
                                        "()J", false);
                            } else {
                                super.visitLdcInsn(cst);
                            }
                        }
                    };
                }
                return super.visitMethod(access, name, desc, signature, exceptions);
            }
        };

        reader.accept(visitor, 0);

        return writer.toByteArray();
    }
}
