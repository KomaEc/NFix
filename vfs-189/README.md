# vfs-189

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