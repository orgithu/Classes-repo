## A14
**Exercise statement:** \(\xi \sim N(0,4)\), \(A=\{|\xi-1|>6\}\), find \(P(A)\).

**Given:** \(\mu=0,\ \sigma=2\).

**What to find:** \(P(|\xi-1|>6)\).

\[
P(|\xi-1|>6)=P(\xi<-5)+P(\xi>7)
\]
\[
=\Phi\!\left(\frac{-5-0}{2}\right)+\left(1-\Phi\!\left(\frac{7-0}{2}\right)\right)
=\Phi(-2.5)+\bigl(1-\Phi(3.5)\bigr)
\]
\[
=0.0062096653+0.0002326291
=0.0064422944
=0.0064
\]

---

## A15~A1
**Exercise statement:** For sample \(A\) \((n=80)\), find the sample mean.

**Given:** \(n=80,\ \sum x_i=249\).

**What to find:** \(\bar M=\bar x\).

\[
\bar x=\frac{1}{n}\sum_{i=1}^{n}x_i
=\frac{249}{80}
=3.1125
\]

---

## A15~A2
**Exercise statement:** For sample \(A\), find the sample standard deviation.

**Given:** \(n=80,\ \bar x=3.1125,\ \sum (x_i-\bar x)^2=187.9875\).

**What to find:** \(\bar S=s\).

\[
s=\sqrt{\frac{1}{n-1}\sum_{i=1}^{n}(x_i-\bar x)^2}
=\sqrt{\frac{187.9875}{79}}
=\sqrt{2.3795886076}
=1.5425915232
=1.5426
\]

---

## A15~B1
**Exercise statement:** For sample \(B\) \((n=212)\), find the median.

**Given:** \(n=212\), ordered values \(x_{(106)}=34,\ x_{(107)}=34\).

**What to find:** \(\bar m_e\).

\[
\bar m_e=\frac{x_{(106)}+x_{(107)}}{2}
=\frac{34+34}{2}
=34.0000
\]

---

## A16~1
**Exercise statement:** For sample \(A\), find the confidence-interval endpoint for \(M_X\), \(\alpha=0.005\).

**Given:** \(n=80,\ \bar x=3.1125,\ s=1.5425915232,\ t_{1-\alpha/2;\,79}=t_{0.9975;79}=2.8880107079\).

**What to find:** Lower endpoint \(L\) of \(M_X\) confidence interval.

\[
L=\bar x-t_{1-\alpha/2;\,n-1}\frac{s}{\sqrt{n}}
\]
\[
=3.1125-2.8880107079\cdot\frac{1.5425915232}{\sqrt{80}}
=2.6144135284
=2.6144
\]

---

## A17~2
**Exercise statement:** For normality test on sample \(B\), find theoretical critical value \(\chi^2_{l,\alpha}\), \(\alpha=0.005\).

**Given:** Number of intervals \(m=10\), estimated parameters \(s=2\) (\(\mu,\sigma\)).

**What to find:** \(\chi^2_{l,\alpha}\).

\[
l=m-s-1=10-2-1=7
\]
\[
\chi^2_{l,\alpha}=\chi^2_{7,\,0.995}=20.277739875=20.2777
\]

---

## A19
**Exercise statement:** Using sample \(C\), build grouped correlation table and find \(r\).

**Given:** \(n=78,\ x_0=22,\ h_x=8,\ y_0=6,\ h_y=14\), grouped table frequencies \(n_{ij}\):

\[
\begin{bmatrix}
3&0&0&0&0&0\\
2&1&0&3&0&0\\
5&6&8&8&7&2\\
5&5&6&3&3&2\\
0&0&0&4&3&1\\
0&0&0&0&0&1
\end{bmatrix}
\]

Class midpoints:
\[
x_i^*=(26,34,42,50,58,66),\quad
y_j^*=(13,27,41,55,69,83)
\]

**What to find:** \(r\).

\[
\bar x=\frac{1}{n}\sum_i n_{i\cdot}x_i^*=45.1794871795,\qquad
\bar y=\frac{1}{n}\sum_j n_{\cdot j}y_j^*=44.5897435897
\]
\[
S_{xy}=\sum_{i,j}n_{ij}(x_i^*-\bar x)(y_j^*-\bar y)=4597.7435897436
\]
\[
S_{xx}=\sum_i n_{i\cdot}(x_i^*-\bar x)^2=4523.4871794872,\qquad
S_{yy}=\sum_j n_{\cdot j}(y_j^*-\bar y)^2=37410.8717948718
\]
\[
r=\frac{S_{xy}}{\sqrt{S_{xx}S_{yy}}}
=\frac{4597.7435897436}{\sqrt{4523.4871794872\cdot37410.8717948718}}
=0.3534346717
=0.3534
\]
