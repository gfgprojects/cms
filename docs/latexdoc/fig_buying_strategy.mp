prologues:=3;

verbatimtex
%&latex 
\documentclass[10pt]{article}
\usepackage{color}
\begin{document}
etex

beginfig(0);
path d,e;
draw(-5cm,4cm)--(-5cm,0)--(-1cm,0);
d=(-5cm,2.0cm)--(-3.0cm,0);
e=(-5cm,1.5cm)--(-1cm,1.5cm);
draw d;
z1=d intersectionpoint e;
draw (-5cm,ypart(z1))-- z1 -- (xpart(z1),0) dashed evenly;
label.lft(btex $p^*_A$ etex,(-5cm,ypart(z1)));
label.bot(btex $q^*_A$ etex,(xpart(z1),0cm));
label.lft(btex $p^+_A$ etex,(-5cm,ypart(z1)+1.8cm));
draw (-5.1cm,ypart(z1)+1.8cm)--(-4.9cm,ypart(z1)+1.8cm);



draw(0cm,4cm)--(0cm,0)--(4cm,0);
d:=(0cm,2.5cm)--(2.5cm,0);
e:=(0cm,2.0cm)--(4cm,2.0cm);
draw d;
z2=d intersectionpoint e;
draw (0cm,ypart(z2))-- z2 -- (xpart(z2),0) dashed evenly;
label.lft(btex $p^*_B$ etex,(0cm,ypart(z2)));
label.bot(btex $q^*_B$ etex,(xpart(z2),0cm));
label.lft(btex $p^+_B$ etex,(0cm,ypart(z2)+0.6cm));
draw (-0.1cm,ypart(z2)+0.6cm)--(0.1cm,ypart(z2)+0.6cm);



draw(5cm,4cm)--(5cm,0)--(9cm,0);
d:=(5cm,2.0cm)--(7.0cm,0);
e:=(5cm,1.5cm)--(9cm,1.5cm);
draw d;
z3=d intersectionpoint e;
draw (5cm,1.5cm)-- z3 -- (xpart(z3),0) dashed evenly;
label.lft(btex $p^*_C$ etex,(5cm,ypart(z3)));
label.bot(btex $q^*_C$ etex,(xpart(z3),0cm));
label.lft(btex $p^+_C$ etex,(5cm,ypart(z1)+1.5cm));
draw (4.9cm,ypart(z1)+1.5cm)--(5.1cm,ypart(z1)+1.5cm);



label.lft(btex $p_A$ etex,(-5cm,3.8cm));
label.lft(btex $p_B$ etex,(0cm,3.8cm));
label.lft(btex $p_C$ etex,(5cm,3.8cm));

label.bot(btex $q_A$ etex,(-1.2cm,0cm));
label.bot(btex $q_B$ etex,(3.8cm,0cm));
label.bot(btex $q_C$ etex,(8.8cm,0cm));


endfig;

beginfig(1);

path d,e;
draw(-5cm,4cm)--(-5cm,0)--(-1cm,0);
d=(-5cm,2.0cm)--(-3.0cm,0);
e=(-5cm,1.5cm)--(-1cm,1.5cm);
draw d;
z1=d intersectionpoint e;
draw (-5cm,ypart(z1))-- z1 -- (xpart(z1),0) dashed evenly;
label.lft(btex $p^*_A$ etex,(-5cm,ypart(z1)));
label.bot(btex $q^*_A$ etex,(xpart(z1),0cm));
label.lft(btex $p^+_A$ etex,(-5cm,ypart(z1)+1.8cm));
draw (-5.1cm,ypart(z1)+1.8cm)--(-4.9cm,ypart(z1)+1.8cm);



draw(0cm,4cm)--(0cm,0)--(4cm,0);
d:=(0cm,2.5cm)--(2.5cm,0);
e:=(0cm,2.0cm)--(4cm,2.0cm);
draw d withcolor 0.4white;
z2=d intersectionpoint e;
draw (0cm,2.0cm)-- z2 -- (xpart(z2),0) dashed evenly;
label.lft(btex $p^*_B$ etex,(0cm,ypart(z2)));
label.bot(btex $q^*_B$ etex,(xpart(z2),0cm));
label.lft(btex $p^+_B$ etex,(0cm,ypart(z2)+0.6cm));
draw (-0.1cm,ypart(z2)+0.6cm)--(0.1cm,ypart(z2)+0.6cm);

draw (0cm,3.0cm)--(3.0cm,0);
drawarrow (1.55cm,1cm)--(1.95cm,1cm);
label(btex $\underbrace{\hskip2mm}$etex, (2.75cm,-0.2cm));
label(btex $q^*_C$etex, (2.75cm,-0.5cm));




draw(5cm,4cm)--(5cm,0)--(9cm,0);
d:=(5cm,2.0cm)--(7.0cm,0);
e:=(5cm,1.5cm)--(9cm,1.5cm);
draw d;
z3=d intersectionpoint e;
draw (5cm,1.5cm)-- z3 -- (xpart(z3),0) dashed evenly;
label.lft(btex $p^*_C$ etex,(5cm,ypart(z3)));
label.bot(btex $q^*_C$ etex,(xpart(z3),0cm));



label.lft(btex $p_A$ etex,(-5cm,3.8cm));
label.lft(btex $p_B$ etex,(0cm,3.8cm));
label.lft(btex $p_C$ etex,(5cm,3.8cm));

label.bot(btex $q_A$ etex,(-1.2cm,0cm));
label.bot(btex $q_B$ etex,(3.8cm,0cm));
label.bot(btex $q_C$ etex,(8.8cm,0cm));


endfig;

beginfig(2);

path d,e;
draw(-5cm,4cm)--(-5cm,0)--(-1cm,0);
d=(-5cm,2.0cm)--(-3.0cm,0);
e=(-5cm,1.5cm)--(-1cm,1.5cm);
draw d withcolor 0.4white;
z1=d intersectionpoint e;
draw (-5cm,ypart(z1))-- z1 -- (xpart(z1),0) dashed evenly;
label.lft(btex $p^*_A$ etex,(-5cm,ypart(z1)));
label.bot(btex $q^*_A$ etex,(xpart(z1),0cm));
label.lft(btex $p^+_A$ etex,(-5cm,ypart(z1)+1.8cm));
draw (-5.1cm,ypart(z1)+1.8cm)--(-4.9cm,ypart(z1)+1.8cm);

draw (-5cm,1.5cm)--(-3.5cm,0);
drawarrow (-4.05cm,1cm)--(-4.45cm,1cm) withcolor 0.0white;



draw(0cm,4cm)--(0cm,0)--(4cm,0);
d:=(0cm,2.5cm)--(2.5cm,0);
e:=(0cm,2.0cm)--(4cm,2.0cm);
draw d withcolor 0.7white;
z2=d intersectionpoint e;
draw (0cm,2.0cm)-- z2 -- (xpart(z2),0) dashed evenly;
label.lft(btex $p^*_B$ etex,(0cm,ypart(z2)));
label.bot(btex $q^*_B$ etex,(xpart(z2),0cm));
label.lft(btex $p^+_B$ etex,(0cm,ypart(z2)+0.6cm));
draw (-0.1cm,ypart(z2)+0.6cm)--(0.1cm,ypart(z2)+0.6cm);

draw (0cm,3.0cm)--(3.0cm,0) withcolor 0.4white;
drawarrow (1.55cm,1cm)--(1.95cm,1cm) withcolor 0.4white;
draw (0cm,3.5cm)--(3.5cm,0) withcolor 0.0white;
drawarrow (2.05cm,1cm)--(2.45cm,1cm) withcolor 0.0white;
label(btex $\underbrace{\hskip2mm}$etex, (3.25cm,-0.2cm));
label(btex $q^*_A$etex, (3.25cm,-0.5cm));

draw(5cm,4cm)--(5cm,0)--(9cm,0) withcolor white;
%d:=(5cm,2.0cm)--(7.0cm,0);
%e:=(5cm,1.5cm)--(9cm,1.5cm);
%draw d;
%z3=d intersectionpoint e;
%draw (5cm,1.5cm)-- z3 -- (xpart(z3),0) dashed evenly;
%label.lft(btex $p^*_C$ etex,(5cm,ypart(z3)));
%label.bot(btex $q^*_C$ etex,(xpart(z3),0cm));



label.lft(btex $p_A$ etex,(-5cm,3.8cm));
label.lft(btex $p_B$ etex,(0cm,3.8cm));
%label.lft(btex $p_C$ etex,(5cm,3.8cm));

label.bot(btex $q_A$ etex,(-1.2cm,0cm));
label.bot(btex $q_B$ etex,(3.8cm,0cm));
%label.bot(btex $q_C$ etex,(8.8cm,0cm));


endfig;

beginfig(3);
path d,e;
draw(-5cm,4cm)--(-5cm,0)--(-1cm,0);
d=(-5cm,2.0cm)--(-3.0cm,0);
e=(-5cm,1.5cm)--(-1cm,1.5cm);
draw d withcolor 0.4white;
z1=d intersectionpoint e;
%draw (-5cm,ypart(z1))-- z1 -- (xpart(z1),0) dashed evenly;
label.lft(btex $p^*_A$ etex,(-5cm,ypart(z1)));
%label.bot(btex $q^*_A$ etex,(xpart(z1),0cm));
label.lft(btex $p^+_A$ etex,(-5cm,ypart(z1)+1.8cm));
draw (-5.1cm,ypart(z1)+1.8cm)--(-4.9cm,ypart(z1)+1.8cm);

draw (-5cm,1.5cm)--(-3.5cm,0);
%drawarrow (-4.05cm,1cm)--(-4.45cm,1cm) withcolor 0.0white;



draw(0cm,4cm)--(0cm,0)--(4cm,0);
d:=(0cm,2.5cm)--(2.5cm,0);
e:=(0cm,2.0cm)--(4cm,2.0cm);
draw d withcolor 0.7white;
z2=d intersectionpoint e;
%draw (0cm,2.0cm)-- z2 -- (xpart(z2),0) dashed evenly;
draw (0cm,ypart(z2)+0.6cm)--(5cm,ypart(z2)+0.6cm) dashed evenly;
label.lft(btex $p^*_B$ etex,(0cm,ypart(z2)));
%label.bot(btex $q^*_B$ etex,(xpart(z2),0cm));
label.lft(btex $p^+_B$ etex,(0cm,ypart(z2)+0.6cm));
draw (-0.1cm,ypart(z2)+0.6cm)--(0.1cm,ypart(z2)+0.6cm);

draw (0cm,3.0cm)--(3.0cm,0) withcolor 0.4white;
draw (0cm,3.5cm)--(3.5cm,0) withcolor 0.0white;
%label(btex $\underbrace{\hskip2mm}$etex, (3.25cm,-0.2cm));
%label(btex $q^*_A$etex, (3.25cm,-0.5cm));




draw(5cm,4cm)--(5cm,0)--(9cm,0);
d:=(5cm,2.0cm)--(7.0cm,0);
e:=(5cm,1.5cm)--(9cm,1.5cm);
draw d;
%z3=d intersectionpoint e;
%draw (5cm,1.5cm)-- z3 -- (xpart(z3),0) dashed evenly;
label.lft(btex $p^+_B-c_D-d$ etex,(5cm,2cm));
draw(4.9cm,2cm)--(5.1cm,2cm);
%label.bot(btex $q^*_C$ etex,(xpart(z3),0cm));



label.lft(btex $p_A$ etex,(-5cm,3.8cm));
label.lft(btex $p_B$ etex,(0cm,3.8cm));
label.lft(btex $p_D$ etex,(5cm,3.8cm));

label.bot(btex $q_A$ etex,(-1.2cm,0cm));
label.bot(btex $q_B$ etex,(3.8cm,0cm));
label.bot(btex $q_D$ etex,(8.8cm,0cm));


endfig;

beginfig(4);
path d,e;
draw(-5cm,4cm)--(-5cm,0)--(-1cm,0);
d=(-5cm,2.0cm)--(-3.0cm,0);
e=(-5cm,1.5cm)--(-1cm,1.5cm);
draw d withcolor 0.4white;
z1=d intersectionpoint e;
%draw (-5cm,ypart(z1))-- z1 -- (xpart(z1),0) dashed evenly;
%label.lft(btex $p^*_A$ etex,(-5cm,ypart(z1)));
%label.bot(btex $q^*_A$ etex,(xpart(z1),0cm));
%label.lft(btex $p^+_A$ etex,(-5cm,ypart(z1)+1.8cm));
%draw (-5.1cm,ypart(z1)+1.8cm)--(-4.9cm,ypart(z1)+1.8cm);

draw (-5cm,1.5cm)--(-3.5cm,0);
pickup pencircle scaled 1pt;
draw (-5cm,1.75cm)--(-3.25cm,0);
pickup defaultpen;

%drawarrow (-4.05cm,1cm)--(-4.45cm,1cm) withcolor 0.0white;



draw(0cm,4cm)--(0cm,0)--(4cm,0);
d:=(0cm,2.5cm)--(2.5cm,0);
e:=(0cm,2.0cm)--(4cm,2.0cm);
draw d withcolor 0.7white;
z2=d intersectionpoint e;
%draw (0cm,2.0cm)-- z2 -- (xpart(z2),0) dashed evenly;
%draw (0cm,2.0cm)--(5cm,ypart(z2)) dashed evenly;
%label.lft(btex $p^*_B$ etex,(0cm,ypart(z2)));
%label.bot(btex $q^*_B$ etex,(xpart(z2),0cm));

draw (0cm,3.0cm)--(3.0cm,0) withcolor 0.4white;
d:=(0cm,3.5cm)--(3.5cm,0);
draw d withcolor 0.0white;
pickup pencircle scaled 1pt;
draw d shifted(3mm,0);
pickup defaultpen;
%label(btex $\underbrace{\hskip2mm}$etex, (3.25cm,-0.2cm));
%label(btex $q^*_A$etex, (3.25cm,-0.5cm));

draw(5cm,4cm)--(5cm,0)--(9cm,0);
d:=(5cm,2.0cm)--(7.0cm,0);
e:=(5cm,1.5cm)--(9cm,1.5cm);
draw d;
pickup pencircle scaled 1pt;
draw (5cm,2.3cm)--(7.3cm,0);
pickup defaultpen;
%z3=d intersectionpoint e;
%draw (5cm,1.5cm)-- z3 -- (xpart(z3),0) dashed evenly;
%label.lft(btex $p^*_C$ etex,(5cm,ypart(z3)));
%label.bot(btex $q^*_C$ etex,(xpart(z3),0cm));



label.lft(btex $p_A$ etex,(-5cm,3.8cm));
label.lft(btex $p_B$ etex,(0cm,3.8cm));
label.lft(btex $p_D$ etex,(5cm,3.8cm));

label.bot(btex $q_A$ etex,(-1.2cm,0cm));
label.bot(btex $q_B$ etex,(3.8cm,0cm));
label.bot(btex $q_D$ etex,(8.8cm,0cm));


endfig;


beginfig(5);
path d,e;

draw(0cm,4cm)--(0cm,0)--(4cm,0);
d:=(0cm,2.0cm)--(2.0cm,0);
e:=(0cm,1.5cm)--(7cm,1.5cm);
draw d;
label.lft(btex $p^*_D$ etex,(0cm,2cm));
draw(-0.1cm,2cm)--(0.1cm,2cm);


draw(5cm,4cm)--(5cm,0)--(9cm,0);
d:=(5cm,2.5cm)--(7.5cm,0);
e:=(5cm,1.5cm)--(9cm,1.5cm);
draw d;
label.lft(btex $p^*_E$ etex,(5cm,2.5cm));
draw(4.9cm,2.5cm)--(5.1cm,2.5cm);


label.lft(btex $p_D$ etex,(0cm,3.8cm));
label.lft(btex $p_E$ etex,(5cm,3.8cm));

label.bot(btex $q_D$ etex,(3.8cm,0cm));
label.bot(btex $q_E$ etex,(8.8cm,0cm));


endfig;






	   end

