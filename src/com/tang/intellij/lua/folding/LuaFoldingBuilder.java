package com.tang.intellij.lua.folding;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.tang.intellij.lua.psi.LuaBlock;
import com.tang.intellij.lua.psi.LuaPsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by tangzx
 * Date : 2015/11/16.
 */
public class LuaFoldingBuilder extends FoldingBuilderEx {
    @NotNull
    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement psiElement, @NotNull Document document, boolean b) {
        List<FoldingDescriptor> descriptors = new ArrayList<FoldingDescriptor>();
        Collection<LuaBlock> luaFuncBodies = PsiTreeUtil.findChildrenOfType(psiElement, LuaBlock.class);
        luaFuncBodies.forEach(block -> {
            PsiElement prev = LuaPsiTreeUtil.getPrevSiblingSkipSpace(block);
            PsiElement next = LuaPsiTreeUtil.getNextSiblingSkipSpace(block);
            TextRange range = block.getTextRange();

            int l = prev != null ? prev.getTextOffset() + prev.getTextLength() : range.getStartOffset();
            int r = next != null ? next.getTextOffset() : range.getEndOffset();

            range = new TextRange(l, r);
            if (range.getLength() > 0) {
                descriptors.add(new FoldingDescriptor(block, range) {
                    @Nullable
                    @Override
                    public String getPlaceholderText() {
                        return "...";
                    }
                });
            }
        });
        return descriptors.toArray(new FoldingDescriptor[descriptors.size()]);
    }

    @Nullable
    @Override
    public String getPlaceholderText(@NotNull ASTNode astNode) {
        return null;
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode astNode) {
        return true;
    }
}
