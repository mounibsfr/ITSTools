byte a[49];
byte peg_count=16;


init { 
 d_step { 
a[0] =2; a[1] =2; a[2] =0; a[3] =0; a[4] =0; a[5] =2; a[6] =2; a[7] =2; a[8] =2; a[9] =0; a[10] =1; a[11] =0; a[12] =2; a[13] =2; a[14] =0; a[15] =0; a[16] =1; a[17] =1; a[18] =1; a[19] =0; a[20] =0; a[21] =0; a[22] =1; a[23] =1; a[24] =1; a[25] =1; a[26] =1; a[27] =0; a[28] =1; a[29] =1; a[30] =1; a[31] =1; a[32] =1; a[33] =1; a[34] =1; a[35] =2; a[36] =2; a[37] =0; a[38] =0; a[39] =0; a[40] =2; a[41] =2; a[42] =2; a[43] =2; a[44] =0; a[45] =0; a[46] =0; a[47] =2; a[48] =2; }
atomic { 
run P();
} }

proctype P() { 

q: if
::  d_step {a[((0)*7+0)]==1 && a[((0)*7+0+1)]==1 && a[((0)*7+0+2)]==0;a[((0)*7+0)] = 0;a[((0)*7+0+1)] = 0;a[((0)*7+0+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((0)*7+1)]==1 && a[((0)*7+1+1)]==1 && a[((0)*7+1+2)]==0;a[((0)*7+1)] = 0;a[((0)*7+1+1)] = 0;a[((0)*7+1+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((0)*7+2)]==1 && a[((0)*7+2+1)]==1 && a[((0)*7+2+2)]==0;a[((0)*7+2)] = 0;a[((0)*7+2+1)] = 0;a[((0)*7+2+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((0)*7+3)]==1 && a[((0)*7+3+1)]==1 && a[((0)*7+3+2)]==0;a[((0)*7+3)] = 0;a[((0)*7+3+1)] = 0;a[((0)*7+3+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((0)*7+4)]==1 && a[((0)*7+4+1)]==1 && a[((0)*7+4+2)]==0;a[((0)*7+4)] = 0;a[((0)*7+4+1)] = 0;a[((0)*7+4+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((1)*7+0)]==1 && a[((1)*7+0+1)]==1 && a[((1)*7+0+2)]==0;a[((1)*7+0)] = 0;a[((1)*7+0+1)] = 0;a[((1)*7+0+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((1)*7+1)]==1 && a[((1)*7+1+1)]==1 && a[((1)*7+1+2)]==0;a[((1)*7+1)] = 0;a[((1)*7+1+1)] = 0;a[((1)*7+1+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((1)*7+2)]==1 && a[((1)*7+2+1)]==1 && a[((1)*7+2+2)]==0;a[((1)*7+2)] = 0;a[((1)*7+2+1)] = 0;a[((1)*7+2+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((1)*7+3)]==1 && a[((1)*7+3+1)]==1 && a[((1)*7+3+2)]==0;a[((1)*7+3)] = 0;a[((1)*7+3+1)] = 0;a[((1)*7+3+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((1)*7+4)]==1 && a[((1)*7+4+1)]==1 && a[((1)*7+4+2)]==0;a[((1)*7+4)] = 0;a[((1)*7+4+1)] = 0;a[((1)*7+4+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((2)*7+0)]==1 && a[((2)*7+0+1)]==1 && a[((2)*7+0+2)]==0;a[((2)*7+0)] = 0;a[((2)*7+0+1)] = 0;a[((2)*7+0+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((2)*7+1)]==1 && a[((2)*7+1+1)]==1 && a[((2)*7+1+2)]==0;a[((2)*7+1)] = 0;a[((2)*7+1+1)] = 0;a[((2)*7+1+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((2)*7+2)]==1 && a[((2)*7+2+1)]==1 && a[((2)*7+2+2)]==0;a[((2)*7+2)] = 0;a[((2)*7+2+1)] = 0;a[((2)*7+2+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((2)*7+3)]==1 && a[((2)*7+3+1)]==1 && a[((2)*7+3+2)]==0;a[((2)*7+3)] = 0;a[((2)*7+3+1)] = 0;a[((2)*7+3+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((2)*7+4)]==1 && a[((2)*7+4+1)]==1 && a[((2)*7+4+2)]==0;a[((2)*7+4)] = 0;a[((2)*7+4+1)] = 0;a[((2)*7+4+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((3)*7+0)]==1 && a[((3)*7+0+1)]==1 && a[((3)*7+0+2)]==0;a[((3)*7+0)] = 0;a[((3)*7+0+1)] = 0;a[((3)*7+0+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((3)*7+1)]==1 && a[((3)*7+1+1)]==1 && a[((3)*7+1+2)]==0;a[((3)*7+1)] = 0;a[((3)*7+1+1)] = 0;a[((3)*7+1+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((3)*7+2)]==1 && a[((3)*7+2+1)]==1 && a[((3)*7+2+2)]==0;a[((3)*7+2)] = 0;a[((3)*7+2+1)] = 0;a[((3)*7+2+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((3)*7+3)]==1 && a[((3)*7+3+1)]==1 && a[((3)*7+3+2)]==0;a[((3)*7+3)] = 0;a[((3)*7+3+1)] = 0;a[((3)*7+3+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((3)*7+4)]==1 && a[((3)*7+4+1)]==1 && a[((3)*7+4+2)]==0;a[((3)*7+4)] = 0;a[((3)*7+4+1)] = 0;a[((3)*7+4+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((4)*7+0)]==1 && a[((4)*7+0+1)]==1 && a[((4)*7+0+2)]==0;a[((4)*7+0)] = 0;a[((4)*7+0+1)] = 0;a[((4)*7+0+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((4)*7+1)]==1 && a[((4)*7+1+1)]==1 && a[((4)*7+1+2)]==0;a[((4)*7+1)] = 0;a[((4)*7+1+1)] = 0;a[((4)*7+1+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((4)*7+2)]==1 && a[((4)*7+2+1)]==1 && a[((4)*7+2+2)]==0;a[((4)*7+2)] = 0;a[((4)*7+2+1)] = 0;a[((4)*7+2+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((4)*7+3)]==1 && a[((4)*7+3+1)]==1 && a[((4)*7+3+2)]==0;a[((4)*7+3)] = 0;a[((4)*7+3+1)] = 0;a[((4)*7+3+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((4)*7+4)]==1 && a[((4)*7+4+1)]==1 && a[((4)*7+4+2)]==0;a[((4)*7+4)] = 0;a[((4)*7+4+1)] = 0;a[((4)*7+4+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((5)*7+0)]==1 && a[((5)*7+0+1)]==1 && a[((5)*7+0+2)]==0;a[((5)*7+0)] = 0;a[((5)*7+0+1)] = 0;a[((5)*7+0+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((5)*7+1)]==1 && a[((5)*7+1+1)]==1 && a[((5)*7+1+2)]==0;a[((5)*7+1)] = 0;a[((5)*7+1+1)] = 0;a[((5)*7+1+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((5)*7+2)]==1 && a[((5)*7+2+1)]==1 && a[((5)*7+2+2)]==0;a[((5)*7+2)] = 0;a[((5)*7+2+1)] = 0;a[((5)*7+2+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((5)*7+3)]==1 && a[((5)*7+3+1)]==1 && a[((5)*7+3+2)]==0;a[((5)*7+3)] = 0;a[((5)*7+3+1)] = 0;a[((5)*7+3+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((5)*7+4)]==1 && a[((5)*7+4+1)]==1 && a[((5)*7+4+2)]==0;a[((5)*7+4)] = 0;a[((5)*7+4+1)] = 0;a[((5)*7+4+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((6)*7+0)]==1 && a[((6)*7+0+1)]==1 && a[((6)*7+0+2)]==0;a[((6)*7+0)] = 0;a[((6)*7+0+1)] = 0;a[((6)*7+0+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((6)*7+1)]==1 && a[((6)*7+1+1)]==1 && a[((6)*7+1+2)]==0;a[((6)*7+1)] = 0;a[((6)*7+1+1)] = 0;a[((6)*7+1+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((6)*7+2)]==1 && a[((6)*7+2+1)]==1 && a[((6)*7+2+2)]==0;a[((6)*7+2)] = 0;a[((6)*7+2+1)] = 0;a[((6)*7+2+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((6)*7+3)]==1 && a[((6)*7+3+1)]==1 && a[((6)*7+3+2)]==0;a[((6)*7+3)] = 0;a[((6)*7+3+1)] = 0;a[((6)*7+3+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((6)*7+4)]==1 && a[((6)*7+4+1)]==1 && a[((6)*7+4+2)]==0;a[((6)*7+4)] = 0;a[((6)*7+4+1)] = 0;a[((6)*7+4+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((0)*7+2)]==1 && a[((0)*7+2-1)]==1 && a[((0)*7+2-2)]==0;a[((0)*7+2)] = 0;a[((0)*7+2-1)] = 0;a[((0)*7+2-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((0)*7+3)]==1 && a[((0)*7+3-1)]==1 && a[((0)*7+3-2)]==0;a[((0)*7+3)] = 0;a[((0)*7+3-1)] = 0;a[((0)*7+3-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((0)*7+4)]==1 && a[((0)*7+4-1)]==1 && a[((0)*7+4-2)]==0;a[((0)*7+4)] = 0;a[((0)*7+4-1)] = 0;a[((0)*7+4-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((0)*7+5)]==1 && a[((0)*7+5-1)]==1 && a[((0)*7+5-2)]==0;a[((0)*7+5)] = 0;a[((0)*7+5-1)] = 0;a[((0)*7+5-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((0)*7+6)]==1 && a[((0)*7+6-1)]==1 && a[((0)*7+6-2)]==0;a[((0)*7+6)] = 0;a[((0)*7+6-1)] = 0;a[((0)*7+6-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((1)*7+2)]==1 && a[((1)*7+2-1)]==1 && a[((1)*7+2-2)]==0;a[((1)*7+2)] = 0;a[((1)*7+2-1)] = 0;a[((1)*7+2-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((1)*7+3)]==1 && a[((1)*7+3-1)]==1 && a[((1)*7+3-2)]==0;a[((1)*7+3)] = 0;a[((1)*7+3-1)] = 0;a[((1)*7+3-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((1)*7+4)]==1 && a[((1)*7+4-1)]==1 && a[((1)*7+4-2)]==0;a[((1)*7+4)] = 0;a[((1)*7+4-1)] = 0;a[((1)*7+4-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((1)*7+5)]==1 && a[((1)*7+5-1)]==1 && a[((1)*7+5-2)]==0;a[((1)*7+5)] = 0;a[((1)*7+5-1)] = 0;a[((1)*7+5-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((1)*7+6)]==1 && a[((1)*7+6-1)]==1 && a[((1)*7+6-2)]==0;a[((1)*7+6)] = 0;a[((1)*7+6-1)] = 0;a[((1)*7+6-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((2)*7+2)]==1 && a[((2)*7+2-1)]==1 && a[((2)*7+2-2)]==0;a[((2)*7+2)] = 0;a[((2)*7+2-1)] = 0;a[((2)*7+2-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((2)*7+3)]==1 && a[((2)*7+3-1)]==1 && a[((2)*7+3-2)]==0;a[((2)*7+3)] = 0;a[((2)*7+3-1)] = 0;a[((2)*7+3-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((2)*7+4)]==1 && a[((2)*7+4-1)]==1 && a[((2)*7+4-2)]==0;a[((2)*7+4)] = 0;a[((2)*7+4-1)] = 0;a[((2)*7+4-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((2)*7+5)]==1 && a[((2)*7+5-1)]==1 && a[((2)*7+5-2)]==0;a[((2)*7+5)] = 0;a[((2)*7+5-1)] = 0;a[((2)*7+5-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((2)*7+6)]==1 && a[((2)*7+6-1)]==1 && a[((2)*7+6-2)]==0;a[((2)*7+6)] = 0;a[((2)*7+6-1)] = 0;a[((2)*7+6-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((3)*7+2)]==1 && a[((3)*7+2-1)]==1 && a[((3)*7+2-2)]==0;a[((3)*7+2)] = 0;a[((3)*7+2-1)] = 0;a[((3)*7+2-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((3)*7+3)]==1 && a[((3)*7+3-1)]==1 && a[((3)*7+3-2)]==0;a[((3)*7+3)] = 0;a[((3)*7+3-1)] = 0;a[((3)*7+3-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((3)*7+4)]==1 && a[((3)*7+4-1)]==1 && a[((3)*7+4-2)]==0;a[((3)*7+4)] = 0;a[((3)*7+4-1)] = 0;a[((3)*7+4-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((3)*7+5)]==1 && a[((3)*7+5-1)]==1 && a[((3)*7+5-2)]==0;a[((3)*7+5)] = 0;a[((3)*7+5-1)] = 0;a[((3)*7+5-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((3)*7+6)]==1 && a[((3)*7+6-1)]==1 && a[((3)*7+6-2)]==0;a[((3)*7+6)] = 0;a[((3)*7+6-1)] = 0;a[((3)*7+6-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((4)*7+2)]==1 && a[((4)*7+2-1)]==1 && a[((4)*7+2-2)]==0;a[((4)*7+2)] = 0;a[((4)*7+2-1)] = 0;a[((4)*7+2-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((4)*7+3)]==1 && a[((4)*7+3-1)]==1 && a[((4)*7+3-2)]==0;a[((4)*7+3)] = 0;a[((4)*7+3-1)] = 0;a[((4)*7+3-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((4)*7+4)]==1 && a[((4)*7+4-1)]==1 && a[((4)*7+4-2)]==0;a[((4)*7+4)] = 0;a[((4)*7+4-1)] = 0;a[((4)*7+4-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((4)*7+5)]==1 && a[((4)*7+5-1)]==1 && a[((4)*7+5-2)]==0;a[((4)*7+5)] = 0;a[((4)*7+5-1)] = 0;a[((4)*7+5-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((4)*7+6)]==1 && a[((4)*7+6-1)]==1 && a[((4)*7+6-2)]==0;a[((4)*7+6)] = 0;a[((4)*7+6-1)] = 0;a[((4)*7+6-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((5)*7+2)]==1 && a[((5)*7+2-1)]==1 && a[((5)*7+2-2)]==0;a[((5)*7+2)] = 0;a[((5)*7+2-1)] = 0;a[((5)*7+2-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((5)*7+3)]==1 && a[((5)*7+3-1)]==1 && a[((5)*7+3-2)]==0;a[((5)*7+3)] = 0;a[((5)*7+3-1)] = 0;a[((5)*7+3-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((5)*7+4)]==1 && a[((5)*7+4-1)]==1 && a[((5)*7+4-2)]==0;a[((5)*7+4)] = 0;a[((5)*7+4-1)] = 0;a[((5)*7+4-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((5)*7+5)]==1 && a[((5)*7+5-1)]==1 && a[((5)*7+5-2)]==0;a[((5)*7+5)] = 0;a[((5)*7+5-1)] = 0;a[((5)*7+5-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((5)*7+6)]==1 && a[((5)*7+6-1)]==1 && a[((5)*7+6-2)]==0;a[((5)*7+6)] = 0;a[((5)*7+6-1)] = 0;a[((5)*7+6-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((6)*7+2)]==1 && a[((6)*7+2-1)]==1 && a[((6)*7+2-2)]==0;a[((6)*7+2)] = 0;a[((6)*7+2-1)] = 0;a[((6)*7+2-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((6)*7+3)]==1 && a[((6)*7+3-1)]==1 && a[((6)*7+3-2)]==0;a[((6)*7+3)] = 0;a[((6)*7+3-1)] = 0;a[((6)*7+3-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((6)*7+4)]==1 && a[((6)*7+4-1)]==1 && a[((6)*7+4-2)]==0;a[((6)*7+4)] = 0;a[((6)*7+4-1)] = 0;a[((6)*7+4-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((6)*7+5)]==1 && a[((6)*7+5-1)]==1 && a[((6)*7+5-2)]==0;a[((6)*7+5)] = 0;a[((6)*7+5-1)] = 0;a[((6)*7+5-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((6)*7+6)]==1 && a[((6)*7+6-1)]==1 && a[((6)*7+6-2)]==0;a[((6)*7+6)] = 0;a[((6)*7+6-1)] = 0;a[((6)*7+6-2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((0)*7+0)]==1 && a[((0+1)*7+0)]==1 && a[((0+2)*7+0)]==0;a[((0)*7+0)] = 0;a[((0+1)*7+0)] = 0;a[((0+2)*7+0)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((0)*7+1)]==1 && a[((0+1)*7+1)]==1 && a[((0+2)*7+1)]==0;a[((0)*7+1)] = 0;a[((0+1)*7+1)] = 0;a[((0+2)*7+1)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((0)*7+2)]==1 && a[((0+1)*7+2)]==1 && a[((0+2)*7+2)]==0;a[((0)*7+2)] = 0;a[((0+1)*7+2)] = 0;a[((0+2)*7+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((0)*7+3)]==1 && a[((0+1)*7+3)]==1 && a[((0+2)*7+3)]==0;a[((0)*7+3)] = 0;a[((0+1)*7+3)] = 0;a[((0+2)*7+3)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((0)*7+4)]==1 && a[((0+1)*7+4)]==1 && a[((0+2)*7+4)]==0;a[((0)*7+4)] = 0;a[((0+1)*7+4)] = 0;a[((0+2)*7+4)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((0)*7+5)]==1 && a[((0+1)*7+5)]==1 && a[((0+2)*7+5)]==0;a[((0)*7+5)] = 0;a[((0+1)*7+5)] = 0;a[((0+2)*7+5)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((0)*7+6)]==1 && a[((0+1)*7+6)]==1 && a[((0+2)*7+6)]==0;a[((0)*7+6)] = 0;a[((0+1)*7+6)] = 0;a[((0+2)*7+6)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((1)*7+0)]==1 && a[((1+1)*7+0)]==1 && a[((1+2)*7+0)]==0;a[((1)*7+0)] = 0;a[((1+1)*7+0)] = 0;a[((1+2)*7+0)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((1)*7+1)]==1 && a[((1+1)*7+1)]==1 && a[((1+2)*7+1)]==0;a[((1)*7+1)] = 0;a[((1+1)*7+1)] = 0;a[((1+2)*7+1)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((1)*7+2)]==1 && a[((1+1)*7+2)]==1 && a[((1+2)*7+2)]==0;a[((1)*7+2)] = 0;a[((1+1)*7+2)] = 0;a[((1+2)*7+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((1)*7+3)]==1 && a[((1+1)*7+3)]==1 && a[((1+2)*7+3)]==0;a[((1)*7+3)] = 0;a[((1+1)*7+3)] = 0;a[((1+2)*7+3)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((1)*7+4)]==1 && a[((1+1)*7+4)]==1 && a[((1+2)*7+4)]==0;a[((1)*7+4)] = 0;a[((1+1)*7+4)] = 0;a[((1+2)*7+4)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((1)*7+5)]==1 && a[((1+1)*7+5)]==1 && a[((1+2)*7+5)]==0;a[((1)*7+5)] = 0;a[((1+1)*7+5)] = 0;a[((1+2)*7+5)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((1)*7+6)]==1 && a[((1+1)*7+6)]==1 && a[((1+2)*7+6)]==0;a[((1)*7+6)] = 0;a[((1+1)*7+6)] = 0;a[((1+2)*7+6)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((2)*7+0)]==1 && a[((2+1)*7+0)]==1 && a[((2+2)*7+0)]==0;a[((2)*7+0)] = 0;a[((2+1)*7+0)] = 0;a[((2+2)*7+0)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((2)*7+1)]==1 && a[((2+1)*7+1)]==1 && a[((2+2)*7+1)]==0;a[((2)*7+1)] = 0;a[((2+1)*7+1)] = 0;a[((2+2)*7+1)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((2)*7+2)]==1 && a[((2+1)*7+2)]==1 && a[((2+2)*7+2)]==0;a[((2)*7+2)] = 0;a[((2+1)*7+2)] = 0;a[((2+2)*7+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((2)*7+3)]==1 && a[((2+1)*7+3)]==1 && a[((2+2)*7+3)]==0;a[((2)*7+3)] = 0;a[((2+1)*7+3)] = 0;a[((2+2)*7+3)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((2)*7+4)]==1 && a[((2+1)*7+4)]==1 && a[((2+2)*7+4)]==0;a[((2)*7+4)] = 0;a[((2+1)*7+4)] = 0;a[((2+2)*7+4)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((2)*7+5)]==1 && a[((2+1)*7+5)]==1 && a[((2+2)*7+5)]==0;a[((2)*7+5)] = 0;a[((2+1)*7+5)] = 0;a[((2+2)*7+5)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((2)*7+6)]==1 && a[((2+1)*7+6)]==1 && a[((2+2)*7+6)]==0;a[((2)*7+6)] = 0;a[((2+1)*7+6)] = 0;a[((2+2)*7+6)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((3)*7+0)]==1 && a[((3+1)*7+0)]==1 && a[((3+2)*7+0)]==0;a[((3)*7+0)] = 0;a[((3+1)*7+0)] = 0;a[((3+2)*7+0)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((3)*7+1)]==1 && a[((3+1)*7+1)]==1 && a[((3+2)*7+1)]==0;a[((3)*7+1)] = 0;a[((3+1)*7+1)] = 0;a[((3+2)*7+1)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((3)*7+2)]==1 && a[((3+1)*7+2)]==1 && a[((3+2)*7+2)]==0;a[((3)*7+2)] = 0;a[((3+1)*7+2)] = 0;a[((3+2)*7+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((3)*7+3)]==1 && a[((3+1)*7+3)]==1 && a[((3+2)*7+3)]==0;a[((3)*7+3)] = 0;a[((3+1)*7+3)] = 0;a[((3+2)*7+3)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((3)*7+4)]==1 && a[((3+1)*7+4)]==1 && a[((3+2)*7+4)]==0;a[((3)*7+4)] = 0;a[((3+1)*7+4)] = 0;a[((3+2)*7+4)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((3)*7+5)]==1 && a[((3+1)*7+5)]==1 && a[((3+2)*7+5)]==0;a[((3)*7+5)] = 0;a[((3+1)*7+5)] = 0;a[((3+2)*7+5)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((3)*7+6)]==1 && a[((3+1)*7+6)]==1 && a[((3+2)*7+6)]==0;a[((3)*7+6)] = 0;a[((3+1)*7+6)] = 0;a[((3+2)*7+6)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((4)*7+0)]==1 && a[((4+1)*7+0)]==1 && a[((4+2)*7+0)]==0;a[((4)*7+0)] = 0;a[((4+1)*7+0)] = 0;a[((4+2)*7+0)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((4)*7+1)]==1 && a[((4+1)*7+1)]==1 && a[((4+2)*7+1)]==0;a[((4)*7+1)] = 0;a[((4+1)*7+1)] = 0;a[((4+2)*7+1)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((4)*7+2)]==1 && a[((4+1)*7+2)]==1 && a[((4+2)*7+2)]==0;a[((4)*7+2)] = 0;a[((4+1)*7+2)] = 0;a[((4+2)*7+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((4)*7+3)]==1 && a[((4+1)*7+3)]==1 && a[((4+2)*7+3)]==0;a[((4)*7+3)] = 0;a[((4+1)*7+3)] = 0;a[((4+2)*7+3)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((4)*7+4)]==1 && a[((4+1)*7+4)]==1 && a[((4+2)*7+4)]==0;a[((4)*7+4)] = 0;a[((4+1)*7+4)] = 0;a[((4+2)*7+4)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((4)*7+5)]==1 && a[((4+1)*7+5)]==1 && a[((4+2)*7+5)]==0;a[((4)*7+5)] = 0;a[((4+1)*7+5)] = 0;a[((4+2)*7+5)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((4)*7+6)]==1 && a[((4+1)*7+6)]==1 && a[((4+2)*7+6)]==0;a[((4)*7+6)] = 0;a[((4+1)*7+6)] = 0;a[((4+2)*7+6)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((2)*7+0)]==1 && a[((2-1)*7+0)]==1 && a[((2-2)*7+0)]==0;a[((2)*7+0)] = 0;a[((2-1)*7+0)] = 0;a[((2-2)*7+0)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((2)*7+1)]==1 && a[((2-1)*7+1)]==1 && a[((2-2)*7+1)]==0;a[((2)*7+1)] = 0;a[((2-1)*7+1)] = 0;a[((2-2)*7+1)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((2)*7+2)]==1 && a[((2-1)*7+2)]==1 && a[((2-2)*7+2)]==0;a[((2)*7+2)] = 0;a[((2-1)*7+2)] = 0;a[((2-2)*7+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((2)*7+3)]==1 && a[((2-1)*7+3)]==1 && a[((2-2)*7+3)]==0;a[((2)*7+3)] = 0;a[((2-1)*7+3)] = 0;a[((2-2)*7+3)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((2)*7+4)]==1 && a[((2-1)*7+4)]==1 && a[((2-2)*7+4)]==0;a[((2)*7+4)] = 0;a[((2-1)*7+4)] = 0;a[((2-2)*7+4)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((2)*7+5)]==1 && a[((2-1)*7+5)]==1 && a[((2-2)*7+5)]==0;a[((2)*7+5)] = 0;a[((2-1)*7+5)] = 0;a[((2-2)*7+5)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((2)*7+6)]==1 && a[((2-1)*7+6)]==1 && a[((2-2)*7+6)]==0;a[((2)*7+6)] = 0;a[((2-1)*7+6)] = 0;a[((2-2)*7+6)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((3)*7+0)]==1 && a[((3-1)*7+0)]==1 && a[((3-2)*7+0)]==0;a[((3)*7+0)] = 0;a[((3-1)*7+0)] = 0;a[((3-2)*7+0)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((3)*7+1)]==1 && a[((3-1)*7+1)]==1 && a[((3-2)*7+1)]==0;a[((3)*7+1)] = 0;a[((3-1)*7+1)] = 0;a[((3-2)*7+1)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((3)*7+2)]==1 && a[((3-1)*7+2)]==1 && a[((3-2)*7+2)]==0;a[((3)*7+2)] = 0;a[((3-1)*7+2)] = 0;a[((3-2)*7+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((3)*7+3)]==1 && a[((3-1)*7+3)]==1 && a[((3-2)*7+3)]==0;a[((3)*7+3)] = 0;a[((3-1)*7+3)] = 0;a[((3-2)*7+3)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((3)*7+4)]==1 && a[((3-1)*7+4)]==1 && a[((3-2)*7+4)]==0;a[((3)*7+4)] = 0;a[((3-1)*7+4)] = 0;a[((3-2)*7+4)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((3)*7+5)]==1 && a[((3-1)*7+5)]==1 && a[((3-2)*7+5)]==0;a[((3)*7+5)] = 0;a[((3-1)*7+5)] = 0;a[((3-2)*7+5)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((3)*7+6)]==1 && a[((3-1)*7+6)]==1 && a[((3-2)*7+6)]==0;a[((3)*7+6)] = 0;a[((3-1)*7+6)] = 0;a[((3-2)*7+6)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((4)*7+0)]==1 && a[((4-1)*7+0)]==1 && a[((4-2)*7+0)]==0;a[((4)*7+0)] = 0;a[((4-1)*7+0)] = 0;a[((4-2)*7+0)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((4)*7+1)]==1 && a[((4-1)*7+1)]==1 && a[((4-2)*7+1)]==0;a[((4)*7+1)] = 0;a[((4-1)*7+1)] = 0;a[((4-2)*7+1)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((4)*7+2)]==1 && a[((4-1)*7+2)]==1 && a[((4-2)*7+2)]==0;a[((4)*7+2)] = 0;a[((4-1)*7+2)] = 0;a[((4-2)*7+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((4)*7+3)]==1 && a[((4-1)*7+3)]==1 && a[((4-2)*7+3)]==0;a[((4)*7+3)] = 0;a[((4-1)*7+3)] = 0;a[((4-2)*7+3)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((4)*7+4)]==1 && a[((4-1)*7+4)]==1 && a[((4-2)*7+4)]==0;a[((4)*7+4)] = 0;a[((4-1)*7+4)] = 0;a[((4-2)*7+4)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((4)*7+5)]==1 && a[((4-1)*7+5)]==1 && a[((4-2)*7+5)]==0;a[((4)*7+5)] = 0;a[((4-1)*7+5)] = 0;a[((4-2)*7+5)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((4)*7+6)]==1 && a[((4-1)*7+6)]==1 && a[((4-2)*7+6)]==0;a[((4)*7+6)] = 0;a[((4-1)*7+6)] = 0;a[((4-2)*7+6)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((5)*7+0)]==1 && a[((5-1)*7+0)]==1 && a[((5-2)*7+0)]==0;a[((5)*7+0)] = 0;a[((5-1)*7+0)] = 0;a[((5-2)*7+0)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((5)*7+1)]==1 && a[((5-1)*7+1)]==1 && a[((5-2)*7+1)]==0;a[((5)*7+1)] = 0;a[((5-1)*7+1)] = 0;a[((5-2)*7+1)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((5)*7+2)]==1 && a[((5-1)*7+2)]==1 && a[((5-2)*7+2)]==0;a[((5)*7+2)] = 0;a[((5-1)*7+2)] = 0;a[((5-2)*7+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((5)*7+3)]==1 && a[((5-1)*7+3)]==1 && a[((5-2)*7+3)]==0;a[((5)*7+3)] = 0;a[((5-1)*7+3)] = 0;a[((5-2)*7+3)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((5)*7+4)]==1 && a[((5-1)*7+4)]==1 && a[((5-2)*7+4)]==0;a[((5)*7+4)] = 0;a[((5-1)*7+4)] = 0;a[((5-2)*7+4)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((5)*7+5)]==1 && a[((5-1)*7+5)]==1 && a[((5-2)*7+5)]==0;a[((5)*7+5)] = 0;a[((5-1)*7+5)] = 0;a[((5-2)*7+5)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((5)*7+6)]==1 && a[((5-1)*7+6)]==1 && a[((5-2)*7+6)]==0;a[((5)*7+6)] = 0;a[((5-1)*7+6)] = 0;a[((5-2)*7+6)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((6)*7+0)]==1 && a[((6-1)*7+0)]==1 && a[((6-2)*7+0)]==0;a[((6)*7+0)] = 0;a[((6-1)*7+0)] = 0;a[((6-2)*7+0)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((6)*7+1)]==1 && a[((6-1)*7+1)]==1 && a[((6-2)*7+1)]==0;a[((6)*7+1)] = 0;a[((6-1)*7+1)] = 0;a[((6-2)*7+1)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((6)*7+2)]==1 && a[((6-1)*7+2)]==1 && a[((6-2)*7+2)]==0;a[((6)*7+2)] = 0;a[((6-1)*7+2)] = 0;a[((6-2)*7+2)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((6)*7+3)]==1 && a[((6-1)*7+3)]==1 && a[((6-2)*7+3)]==0;a[((6)*7+3)] = 0;a[((6-1)*7+3)] = 0;a[((6-2)*7+3)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((6)*7+4)]==1 && a[((6-1)*7+4)]==1 && a[((6-2)*7+4)]==0;a[((6)*7+4)] = 0;a[((6-1)*7+4)] = 0;a[((6-2)*7+4)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((6)*7+5)]==1 && a[((6-1)*7+5)]==1 && a[((6-2)*7+5)]==0;a[((6)*7+5)] = 0;a[((6-1)*7+5)] = 0;a[((6-2)*7+5)] = 1;peg_count = peg_count-1;}  goto q; 

::  d_step {a[((6)*7+6)]==1 && a[((6-1)*7+6)]==1 && a[((6-2)*7+6)]==0;a[((6)*7+6)] = 0;a[((6-1)*7+6)] = 0;a[((6-2)*7+6)] = 1;peg_count = peg_count-1;}  goto q; 

fi;
}
