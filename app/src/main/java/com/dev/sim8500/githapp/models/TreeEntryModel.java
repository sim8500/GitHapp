package com.dev.sim8500.githapp.models;

/**
 * Created by sbernad on 16.03.16.
 */
public class TreeEntryModel {
    public String path;
    public String mode;
    public String sha;
    public int size;
    public GitTreeType type;

    public static enum GitTreeType {
        blob,
        tree,
        commit;

        public String toString() {
            switch(this) {
                case blob:
                    return "File";
                case tree:
                    return "Subtree";
                case commit:
                    return "Subcommit";
                default:
                    return "";
            }
        }

        public int getSortValue() {
            switch(this) {
                case blob:
                    return 2;
                case tree:
                    return 1;
                default:
                    return 0;
            }
        }
    }
}
