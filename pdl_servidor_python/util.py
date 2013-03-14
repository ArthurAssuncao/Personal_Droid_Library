#coding: utf-8

import re
from settings import *

def remove_tags(texto):
    return TAG_RE.sub('', texto)
