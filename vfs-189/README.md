# vfs-189

## Commit
f052896ac6ece2b36c36706604780dbda90b7f41

Parent : 3acd9f3eee0bc74e898e5cb8b50e85fe7a496d70

## Patch
```diff
864a865,867
>         if (base == null) {
>             throw new FileSystemException("Invalid base filename.");
>         }
866c869
<         if (base != null && VFS.isUriStyle() && base.isFile())
---
>         if (VFS.isUriStyle() && base.isFile())
1341c1344
< }
---
> }
\ No newline at end of file
```
